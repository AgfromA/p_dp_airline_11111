package app.entities;

import app.controllers.IntegrationTestBase;
import app.entities.account.Account;
import app.entities.account.Role;
import app.repositories.AccountRepository;
import app.repositories.RoleRepository;
import app.services.AccountServiceImpl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

@Sql({"/sqlQuery/delete-from-tables.sql"})
@TestMethodOrder(MethodOrderer.MethodName.class)
public class CachingBench extends IntegrationTestBase {

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AccountRepository accountRepository;

    Random random = new Random();

    int iterations = 10000;

    public String GetRandomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        return random.ints(leftLimit, rightLimit+1).limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public String GetRandomMail(){
        return String.format("%s@%s.com", GetRandomString(12), GetRandomString(4));
    }

    public void CalculateAvg(ArrayList list, String naming) {
        System.out.printf("%s: %s msec\n", naming, list.stream().mapToLong(l-> (long) l).average().getAsDouble());
    }

    public void CreateRoles() {
        var role = new Role();
        role.setName("ROLE_ADMIN");
        roleRepository.save(role);
        role = new Role();
        role.setName("ROLE_MANAGER");
        roleRepository.save(role);
    }

    @Test
    void A_SaveUsersAndLoad() {
        CreateRoles();
        ArrayList<Long> TimeListFindRole = new ArrayList<>();
        ArrayList<Long> TimeListSaveUser = new ArrayList<>();
        long time_save;
        long time_load;
        //Save users
        for(int i = 1; i <= iterations; i++){
            System.out.printf("\rIteration save users: %s/%s", i, iterations);
            Account user = new Account();
            switch (random.ints(0,2).limit(1).findFirst().getAsInt()){
                case 0:{
                    user = new Account();
                    time_load = System.currentTimeMillis();
                    user.setRoles(Set.of(roleRepository.findByName("ROLE_ADMIN")));//stress search role id any iteration
                    TimeListFindRole.add(System.currentTimeMillis() - time_load);
                }
                case 1:{
                    user = new Account();
                    time_load = System.currentTimeMillis();
                    user.setRoles(Set.of(roleRepository.findByName("ROLE_MANAGER")));
                    TimeListFindRole.add(System.currentTimeMillis() - time_load);
                }
            }
            user.setFirstName(GetRandomString(4));
            user.setLastName(GetRandomString(4));
            user.setPhoneNumber(GetRandomString(4));
            user.setBirthDate(LocalDate.now());
            user.setEmail(GetRandomMail());
            user.setPassword(GetRandomString(8) + "1A@");
            user.setSecurityQuestion(GetRandomString(4));
            user.setAnswerQuestion(GetRandomString(4));

            time_save = System.currentTimeMillis();
            //https://www.baeldung.com/spring-data-jpa-save-saveandflush
            accountRepository.save(user);
            TimeListSaveUser.add(System.currentTimeMillis() - time_save);
        }
        //Show avg
        System.out.println();
        CalculateAvg(TimeListFindRole, "Find role, avg");
        CalculateAvg(TimeListSaveUser, "Save user, avg");
    }

    @Test
    void B1_LoadUsers() {
        ArrayList<Long> TimeListLoadUser = new ArrayList<>();
        long time_load_user;
        //Load users
        for(long i = 1; i <= iterations; i++) {
            System.out.printf("\rIteration load users: %s/%s", i, iterations);
            time_load_user = System.currentTimeMillis();
            accountService.getAccountById(i);
            TimeListLoadUser.add(System.currentTimeMillis() - time_load_user);
        }
        //Show avg
        System.out.println();
        CalculateAvg(TimeListLoadUser, "Load user, avg");
    }

    @Test
    void B2_LoadUsersSecondary() {
        B1_LoadUsers();
    }

    @Test
    void B3_LoadUsersRandomly(){
        ArrayList<Long> TimeListLoadUser = new ArrayList<>();
        long time_load_user;
        long id;
        for(long i = 1; i <= iterations; i++) {
            id = random.longs(1L, (long) iterations).limit(1).findFirst().getAsLong();
            System.out.printf("\rIteration load users: %s/%s", i, iterations);
            time_load_user = System.currentTimeMillis();
            accountService.getAccountById(id);
            TimeListLoadUser.add(System.currentTimeMillis() - time_load_user);
        }
        //Show avg
        System.out.println();
        CalculateAvg(TimeListLoadUser, "Load user randomly, avg");
    }

    @Test
    @Sql(value = {"/sqlQuery/cache-bench-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void Z_Cleanup(){}
}
