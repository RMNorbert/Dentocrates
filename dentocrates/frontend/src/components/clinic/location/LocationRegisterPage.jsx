import React, { useState, useRef, useEffect } from "react";
import { MultiFetch } from "../../../fetch/MultiFetch";
import { useNavigate } from "react-router-dom";
function LocationRegisterPage (){
    const { data } = MultiFetch();
    const navigate = useNavigate();
    const [zipCode, setZipCode] = useState(1000);
    const [city, setCity] = useState('');
    const [isErrorMessageHidden, setIsErrorMessageHidden] = useState(true);
    const isMounted = useRef(true);
    const errorMessage = "Please fill out all required fields, zip code must be between 1000-10.000";
    useEffect(() => {
        return () => {
            isMounted.current = false;
        };
    }, []);

    const handleZipCodeChange = (event) => {
        setZipCode(event.target.value);
    };

    const handleCityChange = (event) => {
        setCity(event.target.value);
    };
    const HandleSubmit = async (e) => {
        e.preventDefault();
        await postRegistration(zipCode,city);
    }


    const postRegistration = async(zipCode, city)=>{
        const locationRegisterUrl = '/location/register';
        let locationData = {
            zipCode: zipCode,
            city: city
        }
        try{
        await data(locationRegisterUrl,'POST', locationData);
        navigate("/home");
        } catch (error) {
            setIsErrorMessageHidden(false);
            console.error('Error:', error);
        }
    };
    return (
        <div className="clinic-register">
        <div className="pageContent">
            <h1 className="clinic-register-title">Location Register</h1>
            <div className="flex justify-center flex-col items-center text-2xl ">
                <form onSubmit={HandleSubmit}>
                    <div className="inputBox">
                        <label htmlFor="zipCode">ZipCode:</label>
                        <input type="number"
                               id="zipCode"
                               value={zipCode}
                               placeholder={"1000 - 10.000"}
                               onChange={handleZipCodeChange} />
                    </div>
                    <div className="inputBox">
                        <label htmlFor="city">City:</label>
                        <input type="text" id="city" value={city} onChange={handleCityChange} />
                        <label style={{ display: isErrorMessageHidden ? "none" : "block" }}>
                            {errorMessage}
                        </label>
                    </div>
                    <button type="submit">Register</button>
                </form>
            </div>
        </div>
        </div>
    );
}
export default LocationRegisterPage;
