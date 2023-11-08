import './MapBox.css'
import './Map.css'
import './ClinicList.css'
import '@mapbox/mapbox-gl-geocoder/dist/mapbox-gl-geocoder.css'
import 'mapbox-gl/dist/mapbox-gl.css';
import mapboxgl from 'mapbox-gl';
import {useEffect, useRef, useState} from 'react'
import { initializeMap} from './helpers/mapInitializer'
import { drawRoute, getFastestRouteDetails } from './helpers/routeProvider'
import {
  addEventListenersToClinicList, addMarkers,
  buildLocationList,
  createClinicProperties
} from './helpers/clinic-mapping'
import {routeSettings} from "./helpers/MapConfig";
import { MultiFetch } from "../../utils/fetch/MultiFetch";
function Map () {
  const { data } = MultiFetch();
  let isLocationListBuilt = false;
  let map = useRef(null);
  let routeType  = routeSettings.DEFAULT_ROUTE_TYPE;
  let color  = routeSettings.DEFAULT_COLOR;
  const placeMarker = useRef(routeSettings.DEFAULT_STATE);
  const [markers, setMarkers] = useState([]);
  let markerDetails = [];
  let fastestRouteDetails;
  function changeRouteType (type) {
    routeType = type;
  }
  function changeMarkerPlacerState () {
    placeMarker.current = !placeMarker.current;
  }
  function changeRouteColour (e) {
    color = e.target.value;
  }
  function removeMarker() {
    setMarkers((prevMarkers) => {
      const updatedMarkers = [...prevMarkers];
      const removedMarker = updatedMarkers.pop();
      removedMarker.remove();
      return updatedMarkers;
    });
  }
  function handleMarkerPlacement (coordinates) {
    if (placeMarker.current == true) {
      if (markers.length <= routeSettings.MAX_NUMBER_OF_MARKERS) {
        const newMarker = new mapboxgl.Marker()
            .setDraggable(true)
            .setLngLat([coordinates.lng, coordinates.lat])
            .addTo(map.current);
        setMarkers((prevMarkers) => [...prevMarkers, newMarker]);
      }
    }
  }

  async function drawFastestRoute () {
    try {
      const coordinates = markers.map(
        (marker) => marker.getLngLat().lng + ',' + marker.getLngLat().lat)
        .join(';', ',')

      fastestRouteDetails = await getFastestRouteDetails(routeType, coordinates)
      markerDetails = fastestRouteDetails.legs;

      drawRoute(fastestRouteDetails, map.current, color)
    } catch (error) {
      console.error(error)
    }
  }
 async function loadMap () {
   try {
     mapboxgl.accessToken = routeSettings.TOKEN
    const clinicDataUrl = `/clinic/all`;
    const clinicData = await data(clinicDataUrl)
    const clinicProperties = createClinicProperties(await clinicData);
    map.current = initializeMap(handleMarkerPlacement, clinicProperties);
    if (!isLocationListBuilt) {
     buildLocationList(map.current, clinicProperties)
     isLocationListBuilt = true;
    }
   addMarkers(map.current, clinicProperties, handleMarkerPlacement)
   addEventListenersToClinicList(clinicProperties,map.current,handleMarkerPlacement)
   }catch (e) {
     console.error(e);
   }
   }

  useEffect(() => {
    loadMap()
  }, [])

  return (
    <div>
    <div className="Map">
      <div className='box'>
        <header>
          <meta name="viewport" content="initial-scale=1,maximum-scale=1,user-scalable=no"/>
          <link href='https://api.mapbox.com/mapbox-gl-js/v2.14.1/mapbox-gl.css' rel='stylesheet' />
        </header>
        <div>
          <div className='settings'>
            <label id="label" >Place marker</label>
            <input id="markerbox" type='checkbox' onClick={() => changeMarkerPlacerState()}/>
            <strong>Route by:</strong>
            <div className='routes'>
              <label id="label">Traffic</label>
              <input id="checkbox" type='checkbox' onClick={ () => changeRouteType('driving-traffic')}/>
              <label id="label">Driving</label>
              <input id="checkbox" type='checkbox' onClick={() => changeRouteType('driving')}/>
              <label id="label">Walking</label>
              <input id="checkbox" type='checkbox' onClick={() => changeRouteType('walking')}/>
              <label id="label">Route color</label>
              <input id="colorbox" className='colour-picker' type='color' defaultValue={color}
                onChange={(e) => changeRouteColour(e)}/>
            </div>
            <button id='plan-route-button' onClick={() => drawFastestRoute()}>Plan route </button>
            <button id='remove-route-button' onClick={() => removeMarker()}>Remove marker</button>
          </div>
        </div>
      </div>
    </div>
    <div className='sidebar'>
      <div className='heading'>
        <h1>Clinic locations</h1>
      </div>
      <div id='listings' className='listings'></div>
    </div>
    <div id="map" className="map-side"></div>
    <div id="instructions" />
  </div>
  )
}

export default Map
