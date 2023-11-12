import mapboxgl from 'mapbox-gl';

export const createClinicProperties = (clinicList) => {
    let data = [];
    for(let i = 0; i < clinicList.length ; i++ ) {
        let currentClinic = clinicList[i];
        data.push(
            {"type": "Feature",
                "id": currentClinic.name + i,
                "geometry": {
                    "id": i + currentClinic.name,
                    "type": "Point",
                    "coordinates": [
                        currentClinic.longitude,
                        currentClinic.latitude
                    ]
                },
                "properties": {
                    "name": currentClinic.name,
                    "contact": currentClinic.contactNumber,
                    "address": currentClinic.street,
                    "city": currentClinic.city,
                    "postalCode": currentClinic.zipCode,
                    "open": currentClinic.openingHours,
                    "type": currentClinic.clinicType.replaceAll("_"," ").toLowerCase(),
                    "longitude":currentClinic.longitude,
                    "latitude":currentClinic.latitude,
                    "id" : i+1
                }
            }
        )
    }
    return {"type": "FeatureCollection", "features":data}
}
function removePopUp() {
    const popUps = document.getElementsByClassName('mapboxgl-popup');
    if (popUps[0]) popUps[0].remove();
}
function findClinicById(clinicId, clinics) {
    for (const clinic of clinics.features) {
        if (clinic.properties.id == clinicId){
            return clinic;
        }
    }
}
function createPopUp(clinic, map, coordinates, addMarker) {
    removePopUp();
    new mapboxgl.Popup({ closeOnClick: false })
        .setLngLat([coordinates.lng,coordinates.lat])
        .setHTML(`<div className="popups"><h3>${clinic.properties.name}</h3>
        <h4>${clinic.properties.address}</h4>
        <h4>Open:${clinic.properties.open}</h4>
        <h4>${clinic.properties.type}</h4>
        <div id="button-list">
        <h2 id="markPopup">📌</h2>
        <h5 id="closePopup">X</h5>
        </div>
        </div>`)
        .addTo(map);

    const closeButton = document.getElementById('closePopup');
    const markPopup = document.getElementById('markPopup');
    if (closeButton) {
        closeButton.addEventListener('click', function () {
            removePopUp();
        });
    }
    if(markPopup) {
        markPopup.addEventListener('click', function () {
            addMarker(coordinates);
            removePopUp();
        });
    }
}
export const buildLocationList = (map,clinics) => {
    const listings = document.getElementById('listings');
    for (const clinic of clinics.features) {
        const listing = document.createElement('div');
        listing.id = `listing-${clinic.properties.id}`;
        listing.className = 'item';
        const link = document.createElement('a');
        link.href = '#';
        link.className = 'title';
        link.id = `link-${clinic.properties.id}`;
        link.innerHTML = `${clinic.properties.name}`;

        const details = document.createElement('div');
        details.innerHTML = `</br>· ${clinic.properties.type}</br>`;
        if(clinic.properties.address){
            details.innerHTML += `</br>· Address: ${clinic.properties.address} ${clinic.properties.city}</br>`;
        }
        if (clinic.properties.contact) {
            details.innerHTML += `</br>· Contact: ${clinic.properties.contact}</br>`;
        }
        if (clinic.properties.open) {
            details.innerHTML += `</br>· Open: ${clinic.properties.open}`;
        }
        listing.appendChild(link);
        listing.appendChild(details);
        listings.appendChild(listing);
    }
}
export const addEventListenersToClinicList = (clinics, map, addMarker) => {
    const listings = document.getElementById('listings');
    listings.addEventListener('click', function (event) {
        if (event.target.classList.contains('title')) {
            const listingId = event.target.id;
            const clinicId = listingId.replace('link-', '');
            const clinic = findClinicById(clinicId,clinics);

            if (clinic) {
                const currentClinicCoordinates = {
                    lng: clinic.properties.longitude,
                    lat: clinic.properties.latitude
                };
                map.flyTo({
                    center:[currentClinicCoordinates.lng,currentClinicCoordinates.lat]
                })
                createPopUp(clinic, map, currentClinicCoordinates, addMarker);
                const activeItem = document.getElementsByClassName('active');
                if (activeItem[0]) {
                    activeItem[0].classList.remove('active');
                }
                event.target.parentNode.classList.add('active');
            }
        }
    });
}
export const addMarkers = (map, clinics, addMarker) => {
    for (const clinic of clinics.features) {
        const el = document.createElement('div');
        el.id = `marker-${clinic.properties.name}`;
        el.className = 'clinic-marker';
        el.addEventListener('click', () => {
            const coordinates = {lng:clinic.properties.longitude, lat:clinic.properties.latitude}
            createPopUp(clinic, map, coordinates, addMarker)
        });
        new mapboxgl.Marker(el, {offset: [0, -23]})
            .setLngLat(clinic.geometry.coordinates)
            .setDraggable(false)
            .addTo(map);
    }
}
