import React, { useState, useRef, useEffect } from "react";
import { MultiFetch } from "../../../fetch/MultiFetch";
import { useNavigate } from "react-router-dom";
function LocationRegisterPage (){
    const { data } = MultiFetch();
    const navigate = useNavigate();
    const [zipCode, setZipCode] = useState(1000);
    const [city, setCity] = useState('');
    const isMounted = useRef(true);
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
        let locationData = {
            zipCode: zipCode,
            city: city
        }
        await data('/location/register','POST', locationData);
        navigate("/home");
    };
    return (
        <div className="pageContent">
            <h1>Location Register</h1>
            <div className="flex justify-center flex-col items-center text-2xl ">
                <form onSubmit={HandleSubmit}>
                    <div>
                        <label htmlFor="zipCode">ZipCode:</label>
                        <input type="number"
                               id="zipCode"
                               value={zipCode}
                               placeholder={"1000 - 10.000"}
                               onChange={handleZipCodeChange} />
                    </div>
                    <div>
                        <label htmlFor="city">City:</label>
                        <input type="text" id="city" value={city} onChange={handleCityChange} />
                    </div>
                    <button type="submit">Register</button>
                </form>
            </div>
        </div>
    );
}
export default LocationRegisterPage;
