package app.controllers.rest;

import app.entities.Route;
import app.services.interfaces.RouteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@ApiIgnore
@Deprecated
@Slf4j
@Api(tags = "Route REST")
@Tag(name = "Route REST", description = "API для операций с маршрутами полетов")
@RequestMapping("/api/routes")
@RestController
@RequiredArgsConstructor
public class RouteRestController {

    private final RouteService routeService;

    @PostMapping
    @ApiOperation(value = "Create new Route")
    public ResponseEntity<Route> createRoute(
            @ApiParam(
                    name = "route",
                    value = "Route model"
            )
            @RequestBody Route route) {
        log.info("addRoute: new Route with id={} added", route.getId());
        return new ResponseEntity<>(routeService.saveRoute(route), HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation(value = "Get list of all Route")
    public ResponseEntity<List<Route>> getAllRoutes() {
        log.info("getAllRoutes : all routes returned");
        return new ResponseEntity<>(routeService.getAllRoutes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get Route by \"id\"")
    public ResponseEntity<Route> getRouteById(
            @ApiParam(
                    name = "id",
                    value = "Route.id"
            )
            @PathVariable Long id) {
        if (routeService.getRouteById(id) == null) {
            log.error("getRouteById: route with id={} doesn't exist.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("getRouteById : returned route with id={}", id);
        return new ResponseEntity<>(routeService.getRouteById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "Edit Route by \"id\"")
    public ResponseEntity<Route> editRouteById(
            @ApiParam(
                    name = "id",
                    value = "Route.id"
            )
            @PathVariable("id") Long id,
            @ApiParam(
                    name = "route",
                    value = "Route model"
            )
            @RequestBody @Valid Route route) {

        if (routeService.getRouteById(id) == null) {
            log.error("editRoute: route with id={} doesn't exist.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        route.setId(id);
        log.info("editRoute: the route with id={} has been edited.", id);
        return new ResponseEntity<>(routeService.saveRoute(route), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete Route by \"id\"")
    public ResponseEntity<HttpStatus> deleteRouteById(
            @ApiParam(
                    name = "id",
                    value = "Route.id"
            )
            @PathVariable("id") Long id) {
        try {
            routeService.deleteRouteById(id);
            log.info("deleteRoute: the route with id={} has been deleted.", id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("deleteRoute: error of deleting - route with id={} not found.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}