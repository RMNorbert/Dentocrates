import "./ClinicRegisterPage.css";
import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Loading } from "../../../lodaingPage/Loading";
import { MultiFetch } from "../../../../utils/fetch/MultiFetch";
import { userId } from "../../../../utils/token/TokenDecoder";

function ClinicRegisterPage (){
    const { data } = MultiFetch();
    const navigate = useNavigate();
    const [name, setName] = useState('');
    const [clinicType, setClinicType] = useState('');
    const [contactNumber, setContactNumber] = useState('');
    const [website, setWebsite] = useState('');
    const [zipCode, setZipCode] = useState(0);
    const [city, setCity] = useState('');
    const [street, setStreet] = useState('');
    const [openingHours, setOpeningHours] = useState('');

    const [isDataLoaded, setIsDataLoaded] = useState(false);
    const [locations, setLocations] = useState([]);
    const [hidden, setHidden] = useState(true);
    const isMounted = useRef(true);
    const [errorMessage, setErrorMessage] = useState([]);
    const getLocationData = async () => {
        const locationDataUrl = `/location/all`;
        const onLoadSelectedClinicType = 'COMMUNITY DENTAL CLINIC';
        const responseData = await data(locationDataUrl);
        setLocations(await responseData);
        setIsDataLoaded(true);
        if (responseData[0]) {
            setZipCode(parseInt(responseData[0].zipCode))
            setCity(responseData[0].city)
        }
        setClinicType(onLoadSelectedClinicType)
    };

    useEffect(() => {
        if(!isDataLoaded){
            getLocationData();
        }
        return () => {
            isMounted.current = false;
        };
    }, []);
    const handleNameChange = (event) => {
        setName(event.target.value);
    };
    const handleClinicTypeChange = (event) => {
        setClinicType(event.target.value);
    };
    const handleContactNumberChange = (event) => {
        setContactNumber(event.target.value);
    };
    const handleWebsiteChange = (event) => {
        setWebsite(event.target.value);
    };
    const handleZipCodeChange = (event) => {
        let chosen = event.target.value.split(" ");
        setZipCode(parseInt(chosen[0]));
        setCity(chosen[2] + " " + chosen[3]);
    };
    const handleStreetChange = (event) => {
        setStreet(event.target.value);
    };
    const handleOpeningHoursChange = (event) => {
        setOpeningHours(event.target.value);
    };
    const HandleSubmit = async (e) => {
        e.preventDefault();
        let registerData = {
            name: name,
            clinicType: clinicType.split(" ").join("_"),
            contactNumber: contactNumber,
            website: website,
            zipCode: zipCode,
            city: city,
            street: street,
            openingHours: openingHours,
            dentistId: userId()
        };
        try {
            const response = await data('/clinic/register', 'Post', registerData);
            setErrorMessage(response.replace(/[A-Z]/g, match => ' ' + match).trim().toLowerCase().split(";"));
            setHidden(false);
            if(response.includes("successfully")) {
                navigate('/home');
            }
        } catch (error) {
            setErrorMessage("Please fill out all the required fields.");
            setHidden(false);
            console.error('Error:', error);
        }
    }

    if(isDataLoaded) {
        return (
            <div className="clinic-register">
            <div className="pageContent">
                <h1 className="register-title">Register Clinic</h1>
                <div className="inputs">
                    <form onSubmit={HandleSubmit}>
                        <div className="inputBox">
                            <label htmlFor="name">Clinic name:</label>
                            <input type="text" id="name" value={name} onChange={handleNameChange}/>
                        </div>
                        <div className="inputBox">
                            <label htmlFor="clinicType">Clinic type:</label>
                            <select id="clinicType" value={clinicType} onChange={handleClinicTypeChange}>
                                <option>COMMUNITY DENTAL CLINIC</option>
                                <option>PRIVATE DENTAL CLINIC</option>
                            </select>
                        </div>
                        <div className="inputBox">
                            <label htmlFor="contactNumber">Contact number:</label>
                            <input type="text" id="contactNumber" value={contactNumber}
                                   onChange={handleContactNumberChange}/>
                        </div>
                        <div className="inputBox">
                            <label htmlFor="website">Website:</label>
                            <input type="text" id="website" value={website} placeholder="Optional to fill" onChange={handleWebsiteChange}/>
                        </div>
                        <div className="inputBox">
                            <label htmlFor="zipCode">Zipcode:</label>
                            <select id="zipCode"  onChange={handleZipCodeChange}>
                                {locations.map((location, index) =>
                                    <option key={index} >{location.zipCode} - {location.city}</option>)
                                }
                            </select>
                        </div>
                        <div className="inputBox">
                            <label htmlFor="street">Street:</label>
                            <input type="text" id="street" value={street} onChange={handleStreetChange}/>
                        </div>
                        <div className="inputBox">
                            <label htmlFor="openingHours">Opening hours:</label>
                            <input type="text" id="openingHours" placeholder="6-18" value={openingHours}
                                   onChange={handleOpeningHoursChange}/>
                        </div>
                        <label style={{ display: hidden ? "none" : "block" }}>
                            {errorMessage.map((message) =>
                                <p key={message}>{message}</p>)}
                        </label>
                        <button className="inputBox" type="submit">
                            Register
                        </button>
                    </form>
                </div>
            </div>
            </div>
        );
    } else {
        return(<Loading/>)
    }
}
export default ClinicRegisterPage;
