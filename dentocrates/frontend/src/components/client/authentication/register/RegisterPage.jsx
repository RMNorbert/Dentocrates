import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Terms } from "../../../termsAndConditions/TermsAndConditions";

function RegisterPage (){
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [operatingLicenceNo, setOperatingLicenceNo] = useState("");
    const [message, setMessage] = useState([]);
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
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
        const dentistUrl = "/api/register/dentist";
        const customerUrl = "/api/register/customer";
        fetch(dentist ? dentistUrl : customerUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
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
                if (response.status === 201) {
                    navigate("/");
                } else if(response.status !== 201){
                    response.text().then(errorMessage => setMessage(errorMessage
                        .replace(/[A-Z]/g, match => ' ' + match).trim().toLowerCase().split(";")));
                    setHidden(false);
                }
            })
            .catch(error => console.error(error));
    };
     return (
            <div className="pageContent  shadowBorder roundBox">
                {!dentist ?
                    <h1 className="register-title">
                        Register
                    </h1>
                    :
                    <h1 className="register-title">
                        Register as Dentist
                    </h1> }
                <div>
                <Terms title="Dentocrates"/>
                </div>
                <div className="inputBox message-box" hidden={hidden}>{message.map((error) =>
                    <h4 className="message">{error}</h4>)}
                </div>
                <div className="flex justify-center flex-col items-center text-2xl ">
                    <form onSubmit={HandleSubmit}>
                        <div className="inputBox">
                            <label htmlFor="email">Email:</label>
                            <input type="text" id="email" value={email} onChange={handleEmailChange}/>
                        </div>
                        <div className="inputBox">
                            <label htmlFor="firstName">First Name:</label>
                            <input type="text" id="firstName" value={firstName} onChange={handleFirstNameChange}/>
                        </div>
                        <div className="inputBox">
                            <label htmlFor="lastName">Last Name:</label>
                            <input type="text" id="lastName" value={lastName} onChange={handleLastNameChange}/>
                        </div>
                        <div className="inputBox">
                            <label htmlFor="password">Password:</label>
                            <input type="password" id="password" value={password} onChange={handlePasswordChange}/>
                        </div>
                        { dentist ?
                            <div className="inputBox">
                                <label htmlFor="operatingLicenceNo">Operating Licence Number:</label>
                                <input type="text" id="operatingLicenceNo" value={operatingLicenceNo} onChange={handleOperatingLicenceNoChange}/>
                            </div>
                            :
                            <></>
                        }
                        <div className="inputBox ">
                        <button
                            className="shadowBorder"
                            type="submit"
                        >
                            Register
                        </button>
                        <></>
                        { !dentist ?
                            <button
                                className="shadowBorder"
                                type="button"
                                onClick={() => setDentist(!dentist)}
                            >
                                Register as Dentist ➔
                            </button>
                            :
                            <button
                                className="shadowBorder"
                                type="button"
                                onClick={() => setDentist(!dentist)}
                            >
                                Register as customer ➔
                            </button>
                        }
                        <button className="register shadowBorder"
                            onClick={() => navigate("/")}
                        >
                            Registered already?
                        </button>
                        </div>
                    </form>
                </div>
            </div>
        );
}
export default RegisterPage;
