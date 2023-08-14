package app.services;

import app.entities.Route;
import app.repositories.RouteRepository;
import app.services.interfaces.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Deprecated
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private RouteRepository routeRepository;

    @Override
    public Route saveRoute(Route route) {
        return routeRepository.save(route);
    }

    @Override
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    @Override
    public Route getRouteById(Long id) {
        return routeRepository.getById(id);
    }

    @Override
    public Route updateRoute(Route route) {
        return routeRepository.save(route);
    }

    @Override
    public void deleteRouteById(Long id) {
        routeRepository.deleteById(id);
    }
}
