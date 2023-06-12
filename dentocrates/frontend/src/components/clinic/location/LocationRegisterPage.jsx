import React, { useState, useRef, useEffect } from "react";

function LocationRegisterPage (){
    const [zipCode, setZipCode] = useState(1000);
    const [city, setCity] = useState('');
    const [message, setMessage] = useState('');
    const [hidden, setHidden] = useState(true);
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
        fetch('/location/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                zipCode: zipCode,
                city: city
            }),
        })
            .then(response => {
                setHidden(false);
                if (response.status === 200) {
                    response.text().then(messages => setMessage(messages));
                } else {
                    response.text().then(errorMessage => setMessage(errorMessage));
                }
            })
            .catch(error => console.error(error));
    };
    return (
        <div className="pageContent">
            <h1>Location Register</h1>
            <div hidden = {hidden}>{message}</div>
            <div className="flex justify-center flex-col items-center text-2xl ">
                <form onSubmit={HandleSubmit}>
                    <div>
                        <label htmlFor="zipCode">ZipCode:</label>
                        <input type="number" id="zipCode" value={zipCode} onChange={handleZipCodeChange} />
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
