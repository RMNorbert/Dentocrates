import "./ClinicRegisterPage.css";
import React, {useState, useRef, useEffect, FormEvent, ChangeEvent} from "react";
import { useNavigate } from "react-router-dom";
import { Loading } from "../elements/Loading";
import { MultiFetch } from "../../fetch/MultiFetch";
import { userId } from "../token/TokenDecoder";

const ClinicRegisterPage: React.FC = () => {
    const { data } = MultiFetch();
    const navigate = useNavigate();
    const [name, setName] = useState<string>('');
    const [clinicType, setClinicType] = useState<string>('');
    const [contactNumber, setContactNumber] = useState<string>('');
    const [website, setWebsite] = useState<string>('');
    const [zipCode, setZipCode] = useState<number>(0);
    const [city, setCity] = useState<string>('');
    const [street, setStreet] = useState<string>('');
    const [openingHours, setOpeningHours] = useState<string>('');

    const [isDataLoaded, setIsDataLoaded] = useState<boolean>(false);
    const [locations, setLocations] = useState<LocationDTO[]>([]);
    const [hidden, setHidden] = useState<boolean>(true);
    const isMounted = useRef<boolean>(true);
    const errorMessage = "Please fill out all the required fields.";
    const getLocationData = async () => {
        const locationDataUrl = `/location/all`;
        const onLoadSelectedClinicType = 'COMMUNITY DENTAL CLINIC';

        try {
            const responseData = await data(locationDataUrl);
            setLocations(responseData);
            setIsDataLoaded(true);
            setZipCode(parseInt(responseData[0].zipCode));
            setCity(responseData[0].city);
            setClinicType(onLoadSelectedClinicType);
        } catch (error) {
            console.log(error)
        }
    };

    useEffect(() => {
        if (!isDataLoaded) {
            getLocationData();
        }
        return () => {
            isMounted.current = false;
        };
    }, []);
    const handleNameChange = (event: ChangeEvent<HTMLInputElement>) => {
        setName(event.target.value);
    };

    const handleClinicTypeChange = (event: ChangeEvent<HTMLSelectElement>) => {
        setClinicType(event.target.value);
    };

    const handleContactNumberChange = (event: ChangeEvent<HTMLInputElement>) => {
        setContactNumber(event.target.value);
    };

    const handleWebsiteChange = (event: ChangeEvent<HTMLInputElement>) => {
        setWebsite(event.target.value);
    };

    const handleZipCodeChange = (event: ChangeEvent<HTMLSelectElement>) => {
        const chosen = event.target.value.split(" ");
        setZipCode(parseInt(chosen[0]));
        setCity(chosen[2] + " " + chosen[3]);
    };

    const handleStreetChange = (event: ChangeEvent<HTMLInputElement>) => {
        setStreet(event.target.value);
    };

    const handleOpeningHoursChange = (event: ChangeEvent<HTMLInputElement>) => {
        setOpeningHours(event.target.value);
    };

    const HandleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const registerData: ClinicData = {
            name: name,
            clinicType: clinicType.split(" ").join("_"),
            contactNumber: contactNumber,
            website: website,
            zipCode: zipCode,
            city: city,
            street: street,
            openingHours: openingHours,
            dentistId: userId() || null,
        };
        try {
            await data('/clinic/register', 'Post', registerData);
            navigate('/home');
        } catch (error) {
            setHidden(false);
            console.error('Error:', error);
        }
    };

    if(isDataLoaded) {
        return (
            <div className="clinic-register">
                <div className="pageContent">
                    <h1 className="clinic-register-title">Register Clinic</h1>
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
                                <input type="text" id="website" value={website} placeholder="Optional to fill" onChange={handleWebsiteChange} />
                            </div>
                            <div className="inputBox">
                                <label htmlFor="zipCode">Zipcode:</label>
                                <select id="zipCode" onChange={handleZipCodeChange}>
                                    {locations.map((location, index) => (
                                        <option key={index}>{location.zipCode} - {location.city}</option>
                                    ))}
                                </select>
                            </div>
                            <div className="inputBox">
                                <label htmlFor="street">Street:</label>
                                <input type="text" id="street" value={street} onChange={handleStreetChange} />
                            </div>
                            <div className="inputBox">
                                <label htmlFor="openingHours">Opening hours:</label>
                                <input type="text" id="openingHours" placeholder="6-18" value={openingHours} onChange={handleOpeningHoursChange} />
                            </div>
                            <label style={{ display: hidden ? "none" : "block" }}>{errorMessage}</label>
                            <button type="submit">Register</button>
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
