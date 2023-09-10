import React, {useState, useRef, useEffect, ChangeEvent, FormEvent} from "react";
import { MultiFetch } from "../../../fetch/MultiFetch";
import { useNavigate } from "react-router-dom";
function LocationRegisterPage (){
    const { data } = MultiFetch();
    const navigate = useNavigate();
    const [zipCode, setZipCode] = useState<number>(1000);
    const [city, setCity] = useState<string>('');
    const [isErrorMessageHidden, setIsErrorMessageHidden] = useState<boolean>(true);
    const isMounted = useRef<boolean>(true);
    const errorMessage = "Please fill out all required fields, zip code must be between 1000-10.000";
    useEffect(() => {
        return () => {
            isMounted.current = false;
        };
    }, []);

    const handleZipCodeChange = (event: ChangeEvent<HTMLInputElement>) => {
        const newValue = parseInt(event.target.value, 10);
        if (!isNaN(newValue)) {
            setZipCode(newValue);
        } else {
            setZipCode(1000);
        }
    };

    const handleCityChange = (event: ChangeEvent<HTMLInputElement>) => {
        setCity(event.target.value);
    };
    const HandleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        await postRegistration(zipCode,city);
    }


    const postRegistration = async(zipCode:number, city:string)=>{
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
