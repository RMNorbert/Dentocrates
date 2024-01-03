import {routeSettings} from "../MapConfig";

export async function getFastestRouteDetails (routeType, coordinates) {
  const response = await fetch(
    `https://api.mapbox.com/directions/v5/mapbox/${routeType}/${coordinates}
    ?annotations=distance,duration&overview=full&geometries=geojson&access_token=${routeSettings.TOKEN}`)

  const routeDetails = await response.json()
  return await routeDetails.routes[routeSettings.INDEX_OF_FASTEST_ROUTE]
}

function createInstructionsForSteps (legs) {
  let instructionOfSteps = ''
  for (const step of legs) {
    const destination = step.summary.split(',').join(' => ', ',')
    const stepDistance = Math.round(step.distance)
    const stepDuration = Math.floor(step.duration / routeSettings.DURATION_CONVERSION_FACTOR_TO_MINUTE)

    instructionOfSteps += `<li id='instruction-item'> ${destination}
                                <div>${stepDistance} meter </div>
                                <div>${stepDuration} min </div>
                             </li>`
  }
  return instructionOfSteps
}

function setInsturctions (data) {
  const instructions = document.getElementById('instructions')
  const tripDistance = Math.round(data.distance)
  const tripDuration = Math.floor(data.duration / routeSettings.DURATION_CONVERSION_FACTOR_TO_MINUTE)
  const tripInstructions = createInstructionsForSteps(data.legs)

  instructions.innerHTML = `<div id='instruction-box' >
                              <strong id="instruction-shadow">Route details:</strong>
                              <br/>
                              <div id="instruction-box-item"></div>
                              <p id="p">
                                <strong>Distance: ${tripDistance} meter
                              </p>
                              <p id="p">
                                <strong>Duration: ${tripDuration} min </strong>
                              </p>
                              <div id="instruction-box-item">
                              <ol id="ol">${tripInstructions}</ol>
                              </div>
                            </div>`
}
export const drawRoute = (data, map, color) => {
  setInsturctions(data)
  const route = data.geometry.coordinates

  const geojson = {
    type: 'Feature',
    properties: {},
    geometry: {
      type: 'LineString',
      coordinates: route
    }
  }

  if (map.getSource('route')) {
    map.setPaintProperty('route', 'line-color', color)
    map.getSource('route').setData(geojson)
  }
}
