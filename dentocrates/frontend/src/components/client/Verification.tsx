import "./ClientPage.css"
import React, {ChangeEvent, useEffect, useState} from "react";
import { email } from "../token/TokenDecoder";
import { useParams } from "react-router-dom";
import { MultiFetch } from "../../fetch/MultiFetch";
function VerifyPage ({ reset }: { reset?: boolean }) {
    const { verificationCode } = useParams();
    const { data } = MultiFetch();
    const [verificationCodeValidated, setVerificationCodeValidated] = useState<boolean>(false);
    const [password, setPassword] = useState<string>('');
    const [passwordVerifier, setPasswordVerifier] = useState<string>('');
    const [showPassword, setShowPassword] = useState<boolean>(false);
    const [message, setMessage] = useState<string>('');

    const validateVerificationCode = async () => {
        const response = await data(`/verify/${verificationCode}`);
        setVerificationCodeValidated(response);
        if(!reset){
                deleteVerification();
        }
    }
    const handlePasswordChange = (event:ChangeEvent<HTMLInputElement>) => {
        setPassword(event.target.value);
    };
    const handlePasswordVerifierChange = (event:ChangeEvent<HTMLInputElement>) => {
        setPasswordVerifier(event.target.value);
    };
    const deleteVerification = async () => {
        const deleteVerifyUrl = '/verify/';
        return await data(deleteVerifyUrl, 'DELETE', verificationCode);
    }
    const postPasswordReset = async(password: string)=>{
        if(password === passwordVerifier) {
            const clientUrl = '/api/reset';
            const request = {
                verificationCode: verificationCode,
                email: email(),
                password: password
            };
            console.log("response")
            const response = await data(clientUrl, 'Post', request);
            if (response.status === 200) {
                await deleteVerification();
            }
        }
        setMessage("Passwords do not match");
    };
    const postClientVerification = async ()=>{
            const verificationUrl = '/api/verify';
            const request = {
                verificationCode: verificationCode,
            };
        console.log("response")
            const response = await data(verificationUrl, 'Post', request);
            if (await response.status === 200) {
                const deleteResponse = await deleteVerification();
                console.log(deleteResponse.status)
            }
        };

    useEffect(() => {
        validateVerificationCode();
        if(!reset) {
            postClientVerification();
        }
        }, [verificationCodeValidated]);

    if(!reset && verificationCodeValidated) {
        return(
        <div className="client-box">
            <div className="client-card"
            >
                Your email has been verified
            </div>
        </div>
        )
    } else if (verificationCodeValidated){
        return (
            <div className="client-box">
                <div className="client-card">
                   <div>
                        <h5>
                            New password
                        </h5>
                       <input
                        type={showPassword ? "text" : "password"}
                        onChange={(e) => handlePasswordChange(e)}
                       />
                       <div>
                       <button
                           onClick={() => setShowPassword(!showPassword)}
                       >
                           Show password
                       </button>
                       </div>
                        <h5>
                            Repeat new password:
                        </h5>
                        <input
                           type={showPassword ? "text" : "password"}
                           onChange={(e) => handlePasswordVerifierChange(e)}
                        />
                       <div>
                           {message.length === 0 ? "***" : message}
                       </div>
                    </div>
                    <div>
                        <button
                            onClick={() => postPasswordReset(password)}
                        >
                            Set new password
                        </button>
                    </div>
                </div>
            </div>
        )
    }
}

export default VerifyPage;
