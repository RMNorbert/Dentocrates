import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";

function RegisterPage (){
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [operatingLicenceNo, setOperatingLicenceNo] = useState('');
    const [message, setMessage] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [dentist, setDentist] = useState(false);
    const [hidden, setHidden] = useState(true);
    const isMounted = useRef(true);
    useEffect(() => {
        return () => {
            isMounted.current = false;
        };
    }, []);

    const handleEmailChange = (event) => {
        setEmail(event.target.value);
    };
    const handleOperatingLicenceNoChange = (event) => {
        setOperatingLicenceNo(event.target.value);
    };
    const handleFirstNameChange = (event) => {
        setFirstName(event.target.value);
    };
    const handleLastNameChange = (event) => {
        setLastName(event.target.value);
    };
    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };
    const HandleSubmit = async (e) => {
        e.preventDefault();
        await postRegistration(email,password);
    }

    const postRegistration = async(email, password)=>{
        const dentistUrl = '/api/register/dentist';
        const customerUrl = '/api/register/customer';
        fetch(dentist ? dentistUrl : customerUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: dentist ? JSON.stringify({
                email: email,
                password: password,
                firstName: firstName,
                lastName: lastName,
                operatingLicenceNo : operatingLicenceNo
            }) :
                JSON.stringify({
                email: email,
                password: password,
                firstName: firstName,
                lastName: lastName
            }),
        })
            .then(response => {
                if (response.status === 200) {
                    navigate("/");
                } else {
                    setHidden(false);
                    response.text().then(errorMessage => setMessage(errorMessage));
                }
            })
            .catch(error => console.error(error));
    };
    if(!dentist) {
        return (
            <div className="pageContent">
                <h1>Register</h1>
                <div hidden={hidden}>{message}</div>
                <div className="flex justify-center flex-col items-center text-2xl ">
                    <form onSubmit={HandleSubmit}>
                        <div>
                            <label htmlFor="email">Email:</label>
                            <input type="text" id="email" value={email} onChange={handleEmailChange}/>
                        </div>
                        <div>
                            <label htmlFor="firstName">First Name:</label>
                            <input type="text" id="firstName" value={firstName} onChange={handleFirstNameChange}/>
                        </div>
                        <div>
                            <label htmlFor="lastName">Last Name:</label>
                            <input type="text" id="lastName" value={lastName} onChange={handleLastNameChange}/>
                        </div>
                        <div>
                            <label htmlFor="password">Password:</label>
                            <input type="password" id="password" value={password} onChange={handlePasswordChange}/>
                        </div>
                        <button type="submit">Register</button>
                        <></>
                        <button type="button"  onClick={() => setDentist(!dentist)}>Register as Dentist ➔</button>
                        <h3 className="register"
                            onClick={() => navigate("/")}
                        >
                            Registered already?
                        </h3>
                    </form>
                </div>
            </div>
        );
    } else {
        return (
            <div className="pageContent">
                <h1>Register as Dentist</h1>
                <div hidden={hidden}>{message}</div>
                <div className="flex justify-center flex-col items-center text-2xl ">
                    <form onSubmit={HandleSubmit}>
                        <div>
                            <label htmlFor="email">Email:</label>
                            <input type="text" id="email" value={email} onChange={handleEmailChange}/>
                        </div>
                        <div>
                            <label htmlFor="firstName">First Name:</label>
                            <input type="text" id="firstName" value={firstName} onChange={handleFirstNameChange}/>
                        </div>
                        <div>
                            <label htmlFor="lastName">Last Name:</label>
                            <input type="text" id="lastName" value={lastName} onChange={handleLastNameChange}/>
                        </div>
                        <div>
                            <label htmlFor="password">Password:</label>
                            <input type="password" id="password" value={password} onChange={handlePasswordChange}/>
                        </div>
                        <div>
                            <label htmlFor="operatingLicenceNo">Operating Licence Number:</label>
                            <input type="text" id="operatingLicenceNo" value={operatingLicenceNo} onChange={handleOperatingLicenceNoChange}/>
                        </div>
                        <button type="submit">Register</button>
                        <button type="button"  onClick={() => setDentist(!dentist)}>Register as customer ➔</button>
                        <h3 className="register"
                            onClick={() => navigate("/")}
                        >
                            Registered already?
                        </h3>
                    </form>
                </div>
            </div>
        );
    }
}
export default RegisterPage;
