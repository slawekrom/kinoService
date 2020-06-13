import service.MovieService;
import service.ReservationService;
import service.ShowingService;
import service.UserService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class ApplicationConfig extends Application{
    @Override
    public Set<Class<?>> getClasses() {
        HashSet h = new HashSet<Class<?>>();
        h.add( MovieService.class );
        h.add(ShowingService.class);
        h.add(UserService.class);
        h.add(ReservationService.class);
        return h;
    }
}