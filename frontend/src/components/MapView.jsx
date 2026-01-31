// React 19 + React-Leaflet 5 compatible MapView

import { MapContainer, TileLayer, Marker, Polyline, Popup } from "react-leaflet";
import L from "leaflet";

import iconUrl from "leaflet/dist/images/marker-icon.png";
import iconRetinaUrl from "leaflet/dist/images/marker-icon-2x.png";
import shadowUrl from "leaflet/dist/images/marker-shadow.png";

L.Icon.Default.mergeOptions({
  iconUrl,
  iconRetinaUrl,
  shadowUrl,
});

export default function MapView({ stops = [], hops = [] }) {
  const coords = stops
    .filter(s => s.latitude && s.longitude)
    .map(s => [s.latitude, s.longitude]);

  const center = coords.length ? coords[0] : [12.9716, 77.5946];

  return (
    <div style={{ height: 350, width: "100%", marginTop: 20 }}>
      <MapContainer center={center} zoom={12} style={{ height: "100%", width: "100%" }}>
        <TileLayer
          attribution='© OpenStreetMap contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />

        {/* Markers */}
        {stops.map((stop, idx) => (
          <Marker key={idx} position={[stop.latitude, stop.longitude]}>
            <Popup>
              <strong>{stop.location}</strong>
              {hops[idx] && (
                <>
                  <br />Cost: ₹{hops[idx].cost}
                  <br />Mode: {hops[idx].mode}
                </>
              )}
            </Popup>
          </Marker>
        ))}

        {/* Polyline */}
        {coords.length > 1 && (
          <Polyline positions={coords} color="#1E74D6" weight={5} />
        )}
      </MapContainer>
    </div>
  );
}
