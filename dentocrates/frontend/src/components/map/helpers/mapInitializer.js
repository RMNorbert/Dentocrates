import './PopUp.css'
import mapboxgl from 'mapbox-gl';
import MapboxGeocoder from '@mapbox/mapbox-gl-geocoder'
import {mapSettings, routeSettings} from "./MapConfig";

export const initializeMap = (addMarker, clinicProperties) => {
  const geocoder = createGeocoder(addMarker)

  const map = new mapboxgl.Map({
    container: mapSettings.CONTAINER_ID,
    style: mapSettings.MAP_STYLE,
    center: [mapSettings.CENTER_COORDINATES.longitude, mapSettings.CENTER_COORDINATES.latitude],
    zoom: mapSettings.ZOOM_LEVEL
  }).on('click', (event) => {
    const mouseCoordinate = event.lngLat.wrap()
    addMarker(mouseCoordinate);
  }).addControl(new mapboxgl.NavigationControl(), 'bottom-right')
    .addControl(geocoder)

  map.on('load', () => {
    map.addLayer({
      id: 'locations',
      type: 'circle',
      source: {
        type: 'geojson',
        data: clinicProperties
      }
      });
    createRouteLayer(map);
  })
  return map;
}

const createGeocoder = (addMarker) => {
  return new MapboxGeocoder({
    accessToken: mapboxgl.accessToken,
    mapboxgl: mapboxgl
  }).on('result', (event) => {
    const resultLongitude = event.result.center[0]
    const resultLatitude = event.result.center[1]
    const geocoderResultCoordinates = { lng: resultLongitude, lat: resultLatitude }

    addMarker(geocoderResultCoordinates)
  })
}

export const createRouteLayer = (map) => {
  const geojson = {
    type: 'Feature',
    properties: {},
    geometry: {
      type: 'LineString',
      coordinates: []
    }
  }
  map.addLayer({
    id: 'route',
    type: 'line',
    source: {
      type: 'geojson',
      data: geojson
    },
    layout: {
      'line-join': 'round',
      'line-cap': 'round',
      'visibility': 'visible',
    },
    paint: {
      'line-color': routeSettings.DEFAULT_COLOR,
      'line-width': routeSettings.LINE_WIDTH_IN_PIXELS,
      'line-opacity': routeSettings.LINE_WIDTH_OPACITY
    }
  })
}
