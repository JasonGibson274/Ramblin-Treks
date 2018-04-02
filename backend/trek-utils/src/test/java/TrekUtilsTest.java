import org.junit.Test;
import trek_utils.TrekUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TrekUtilsTest {

    @Test
    public void getDistance() {
        double latitudeStart = 2;
        double longitudeStart = -5;
        double latitudeEnd = 7;
        double longitudeEnd = -8;

        double result = TrekUtils.getDistanceInMetersHaversine(latitudeStart, longitudeStart, latitudeEnd, longitudeEnd);

        assertThat(result, is(648514.1629580036));
    }

    @Test
    public void getBearing() {
        double latitudeStart = 2;
        double longitudeStart = -5;
        double latitudeEnd = 7;
        double longitudeEnd = -8;

        double result = TrekUtils.getBearing(latitudeStart, longitudeStart, latitudeEnd, longitudeEnd);

        assertThat(result, is(345.29031607717945));
    }
}
