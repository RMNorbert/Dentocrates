import "./ClientPage.css"
import React, {ChangeEvent, useEffect, useState} from "react";
import { email } from "../token/TokenDecoder";
import { useParams } from "react-router-dom";
import { MultiFetch } from "../../fetch/MultiFetch";
function VerifyPage ({ reset }: { reset?: boolean }) {
    const { verificationCode } = useParams();
    const [verificationCodeValidated, setVerificationCodeValidated] = useState<boolean>(false);
    const [password, setPassword] = useState<string>('');
    const [passwordVerifier, setPasswordVerifier] = useState<string>('');
    const [showPassword, setShowPassword] = useState<boolean>(false);
    const [message, setMessage] = useState<string>('');

    const validateVerificationCode = async () => {
        const response = await MultiFetch<boolean>(`/verify/${verificationCode}`);
        setVerificationCodeValidated(response);
        if(!reset){
                await deleteVerification();
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
        return await MultiFetch<string>(deleteVerifyUrl, 'DELETE', verificationCode);
    }
    const postPasswordReset = async(password: string)=>{
        if(password === passwordVerifier) {
            const clientUrl = '/api/reset';
            const request = {
                verificationCode: verificationCode,
                email: email(),
                password: password
            };
            const response = await MultiFetch<object>(clientUrl, 'Post', request);
            if (response) {
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
            const response = await MultiFetch(verificationUrl, 'Post', request);
            if (response) {
                await deleteVerification();
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
