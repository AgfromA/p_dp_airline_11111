/**
 *
 */
package provider.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.*;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class CustomUserStorageProvider implements UserStorageProvider,
        UserLookupProvider,
        CredentialInputValidator,
        UserQueryProvider {

    private static final Logger log = LoggerFactory.getLogger(CustomUserStorageProvider.class);
    private KeycloakSession ksession;
    private ComponentModel model;


    public CustomUserStorageProvider(KeycloakSession ksession, ComponentModel model) {
        this.ksession = ksession;
        this.model = model;
    }


    @Override
    public void close() {
        log.info("[I30] close()");
    }


    @Override
    public UserModel getUserById(RealmModel realm, String id) {
        log.info("[I35] getUserById({})", id);
        StorageId sid = new StorageId(id);
        return getUserByUsername(realm, sid.getExternalId());
    }

    @Override
    public UserModel getUserByUsername(RealmModel realm, String username) {
        log.info("[I41] getUserByUsername({})", username);
        try (Connection c = DbUtil.getConnection(this.model)) {
            PreparedStatement st = c.prepareStatement("select username, first_name,last_name, email, birth_date from account where username = ?");
            st.setString(1, username);
            st.execute();
            ResultSet rs = st.getResultSet();
            PreparedStatement roles = c.prepareStatement("SELECT r.role_name\n" +
                                                         "FROM roles r\n" +
                                                         "JOIN account_roles ra ON r.role_id = ra.role_id\n" +
                                                         "JOIN account a ON ra.account_id = a.account_id\n" +
                                                         "WHERE a.username = ?");
            roles.setString(1, username);
            roles.execute();
            ResultSet rsRoles = roles.getResultSet();
            if (rs.next()) {
                return mapUser(realm, rs, rsRoles);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Database error:" + ex.getMessage(), ex);
        }
    }

    @Override
    public UserModel getUserByEmail(RealmModel realm, String email) {
        log.info("[I48] getUserByEmail({})", email);
        try (Connection c = DbUtil.getConnection(this.model)) {
            PreparedStatement st = c.prepareStatement("select username, first_name,last_name, email, birth_date from account where email = ?");
            st.setString(1, email);
            st.execute();
            ResultSet rs = st.getResultSet();
            PreparedStatement roles = c.prepareStatement("SELECT r.role_name\n" +
                                                         "FROM roles r\n" +
                                                         "JOIN account_roles ra ON r.role_id = ra.role_id\n" +
                                                         "JOIN account a ON ra.account_id = a.account_id\n" +
                                                         "WHERE a.email = ?");
            roles.setString(1, email);
            roles.execute();
            ResultSet rsRoles = roles.getResultSet();
            if (rs.next()) {
                return mapUser(realm, rs, rsRoles);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Database error:" + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        log.info("[I57] supportsCredentialType({})", credentialType);
        return PasswordCredentialModel.TYPE.endsWith(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        log.info("[I57] isConfiguredFor(realm={},user={},credentialType={})", realm.getName(), user.getUsername(), credentialType);
        // In our case, password is the only type of credential, so we allways return 'true' if
        // this is the credentialType
        return supportsCredentialType(credentialType);
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
        log.info("[I57] isValid(realm={},user={},credentialInput.type={})", realm.getName(), user.getUsername(), credentialInput.getType());
        if (!this.supportsCredentialType(credentialInput.getType())) {
            return false;
        }
        StorageId sid = new StorageId(user.getId());
        String username = sid.getExternalId();

        try (Connection c = DbUtil.getConnection(this.model)) {
            PreparedStatement st = c.prepareStatement("select password from account where username = ?");
            st.setString(1, username);
            st.execute();
            ResultSet rs = st.getResultSet();
            if (rs.next()) {
                String pwd = rs.getString(1);
                String providedPassword = credentialInput.getChallengeResponse();
                return new BCryptPasswordEncoder().matches(providedPassword, pwd);
            } else {
                return false;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Database error:" + ex.getMessage(), ex);
        }
    }


    // UserQueryProvider implementation

    @Override
    public int getUsersCount(RealmModel realm) {
        log.info("[I93] getUsersCount: realm={}", realm.getName());
        try (Connection c = DbUtil.getConnection(this.model)) {
            Statement st = c.createStatement();
            st.execute("select count(*) from account");
            ResultSet rs = st.getResultSet();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException ex) {
            throw new RuntimeException("Database error:" + ex.getMessage(), ex);
        }
    }

    @Override
    public Stream<UserModel> getGroupMembersStream(RealmModel realm, GroupModel group, Integer firstResult, Integer maxResults) {
        log.info("[I113] getUsers: realm={}", realm.getName());

        try (Connection c = DbUtil.getConnection(this.model)) {
            PreparedStatement st = c.prepareStatement("select username, first_name,last_name, email, birth_date from account order by username limit ? offset ?");
            st.setInt(1, maxResults);
            st.setInt(2, firstResult);
            st.execute();
            ResultSet rs = st.getResultSet();
            List<UserModel> users = new ArrayList<>();

            while (rs.next()) {
                users.add(mapUser(realm, rs, null));
            }
            return users.stream();
        } catch (SQLException ex) {
            throw new RuntimeException("Database error:" + ex.getMessage(), ex);
        }
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realm, String search, Integer firstResult, Integer maxResults) {
        log.info("[I139] searchForUser: realm={}", realm.getName());

        try (Connection c = DbUtil.getConnection(this.model)) {
            PreparedStatement st = c.prepareStatement("select username, first_name,last_name, email, birth_date from account where username like ? order by username limit ? offset ?");
            st.setString(1, search);
            st.setInt(2, maxResults);
            st.setInt(3, firstResult);
            st.execute();
            ResultSet rs = st.getResultSet();
            List<UserModel> users = new ArrayList<>();
            while (rs.next()) {
                users.add(mapUser(realm, rs, null));
            }
            return users.stream();
        } catch (SQLException ex) {
            throw new RuntimeException("Database error:" + ex.getMessage(), ex);
        }
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realm, Map<String, String> params, Integer firstResult, Integer maxResults) {
        return getGroupMembersStream(realm, null, firstResult, maxResults);
    }

    @Override
    public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realm, String attrName, String attrValue) {
        log.info("[I142] searchForUserByUserAttribute: realm={}", realm.getName());

        try (Connection c = DbUtil.getConnection(this.model)) {
            PreparedStatement st = c.prepareStatement("select username, first_name,last_name, email, birth_date from account where ? like ?");
            st.setString(1, attrName);
            st.setString(2, attrValue);
            st.execute();
            ResultSet rs = st.getResultSet();
            List<UserModel> users = new ArrayList<>();
            while (rs.next()) {
                users.add(mapUser(realm, rs, null));
            }
            return users.stream();
        } catch (SQLException ex) {
            throw new RuntimeException("Database error:" + ex.getMessage(), ex);
        }
    }

    //------------------- Implementation 
    private UserModel mapUser(RealmModel realm, ResultSet rs, ResultSet rolesRs) throws SQLException {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        CustomUser user = new CustomUser.Builder(ksession, realm, model, rs.getString("username"))
                .email(rs.getString("email"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .birthDate(rs.getDate("birth_date"))
                .build();
        if (rolesRs != null) {
            user.setRoles(rolesRs);
        }
        return user;
    }
}
