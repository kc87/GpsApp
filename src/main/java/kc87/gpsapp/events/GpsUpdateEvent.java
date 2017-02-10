package kc87.gpsapp.events;

import kc87.gpsapp.util.NmeaParser;

public class GpsUpdateEvent {
   public NmeaParser.Data data;
   public NmeaParser.State state;

   public GpsUpdateEvent(final NmeaParser.Data data, final NmeaParser.State state) {
      this.data = data;
      this.state = state;
   }
}
