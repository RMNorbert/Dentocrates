import React, {useState, useRef, useEffect, ChangeEvent, FormEvent} from "react";
import { useNavigate } from "react-router-dom";
import { Terms } from "../elements/TermsAndConditions";

function RegisterPage (){
    const navigate = useNavigate();
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [operatingLicenceNo, setOperatingLicenceNo] = useState<string>('');
    const [message, setMessage] = useState<string>('');
    const [firstName, setFirstName] = useState<string>('');
    const [lastName, setLastName] = useState<string>('');
    const [dentist, setDentist] = useState<boolean>(false);
    const [hidden, setHidden] = useState<boolean>(true);
    const isMounted = useRef<boolean>(true);

    useEffect(() => {
        return () => {
            isMounted.current = false;
        };
    }, []);

    const handleEmailChange = (event:ChangeEvent<HTMLInputElement>) => {
        setEmail(event.target.value);
    };

    const handleOperatingLicenceNoChange = (event:ChangeEvent<HTMLInputElement>) => {
        setOperatingLicenceNo(event.target.value);
    };

    const handleFirstNameChange = (event:ChangeEvent<HTMLInputElement>) => {
        setFirstName(event.target.value);
    };

    const handleLastNameChange = (event:ChangeEvent<HTMLInputElement>) => {
        setLastName(event.target.value);
    };

    const handlePasswordChange = (event:ChangeEvent<HTMLInputElement>) => {
        setPassword(event.target.value);
    };

    const HandleSubmit = async (e:FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        await postRegistration(email,password);
    }
    const postRegistration = async(email:string, password:string)=>{
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
                    navigate('/');
                } else {
                    setHidden(false);
                    response.text().then(errorMessage => setMessage(errorMessage));
                }
            })
            .catch(error => console.error(error));
    };
     return (
            <div className="pageContent">
                {!dentist ? <h1>Register</h1> : <h1>Register as Dentist</h1> }
                <div>
                <Terms title="Dentocrates"/>
                </div>
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
                        { dentist ?
                            <div>
                                <label htmlFor="operatingLicenceNo">Operating Licence Number:</label>
                                <input type="text" id="operatingLicenceNo" value={operatingLicenceNo} onChange={handleOperatingLicenceNoChange}/>
                            </div>
                            :
                            <></>
                        }
                        <button type="submit">Register</button>
                        <></>
                        { !dentist ?
                            <button type="button"  onClick={() => setDentist(!dentist)}>Register as Dentist ➔</button>
                            :
                            <button type="button"  onClick={() => setDentist(!dentist)}>Register as customer ➔</button>
                        }
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
export default RegisterPage;
