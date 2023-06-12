import "./ClinicRegisterPage.css";
import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function ClinicRegisterPage (){
    const navigate = useNavigate();
    const [name, setName] = useState('');
    const [clinicType, setClinicType] = useState('');
    const [contactNumber, setContactNumber] = useState('');
    const [website, setWebsite] = useState('');
    const [zipCode, setZipCode] = useState(0);
    const [city, setCity] = useState('');
    const [street, setStreet] = useState('');
    const [dentistInContract, setDentistInContract] = useState(0);
    const [openingHours, setOpeningHours] = useState('');

    const [message, setMessage] = useState('');
    const [hidden, setHidden] = useState(true);
    const isMounted = useRef(true);
    useEffect(() => {
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
        setZipCode(event.target.value);
    };

    const handleCityChange = (event) => {
        setCity(event.target.value);
    };

    const handleStreetChange = (event) => {
        setStreet(event.target.value);
    };

    const handleDentistInContractChange = (event) => {
        setDentistInContract(event.target.value);
    };

    const handleOpeningHoursChange = (event) => {
        setOpeningHours(event.target.value);
    };

    const HandleSubmit = async (e) => {
        e.preventDefault();
        await postRegistration(
            name,
            clinicType,
            contactNumber,
            website,
            zipCode,
            city,
            street,
            dentistInContract,
            openingHours
        );
    }


    const postRegistration = async(name,clinicType,contactNumber,website,zipCode,city,street,dentistInContract,openingHours
    )=>{
        fetch('/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                name: name,
                clinicType: clinicType,
                contactNumber: contactNumber,
                website: website,
                zipCode: zipCode,
                city: city,
                street: street,
                dentistInContract: dentistInContract,
                openingHours: openingHours
            }),
        })
            .then(response => {
                if (response.status === 200) {
                    setHidden(false);
                    response.text().then(messages => setMessage(messages));
                } else {
                    setHidden(false);
                    response.text().then(errorMessage => setMessage(errorMessage));
                }
            })
            .catch(error => console.error(error));
    };
    return (
        <div className="pageContent">
            <h1>Register Clinic</h1>
            <div hidden = {hidden}>{message}</div>
            <div className="inputs">
                <form onSubmit={HandleSubmit}>
                    <div className="inputBox">
                        <label htmlFor="name">Clinic name:</label>
                        <input type="text" id="name" value={name} onChange={handleNameChange} />
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
                        <input type="text" id="contactNumber" value={contactNumber} onChange={handleContactNumberChange} />
                    </div>
                    <div className="inputBox">
                        <label htmlFor="website">Website:</label>
                        <input type="text" id="website" value={website} onChange={handleWebsiteChange} />
                    </div>
                    <div className="inputBox">
                        <label htmlFor="zipCode">Zipcode:</label>
                        <select type="text" id="zipCode" value={zipCode} onChange={handleZipCodeChange} />
                    </div>
                    <div className="inputBox">
                        <label htmlFor="street">Street:</label>
                        <input type="text" id="street" value={street} onChange={handleStreetChange} />
                    </div>
                    <div className="inputBox">
                        <label htmlFor="openingHours">Opening hours:</label>
                        <input type="text" id="openingHours" placeholder="6-18" value={openingHours} onChange={handleOpeningHoursChange} />
                    </div>
                    <button type="submit">Register</button>
                    <h3 className="register"
                        onClick={() =>  navigate("/login")}
                    >
                        Registered already?
                    </h3>
                </form>
            </div>
        </div>
    );
};
export default ClinicRegisterPage;
