import {useNavigate} from "react-router-dom";
import React, {useEffect, useState} from "react";
import { useLocation } from 'react-router-dom';
import {Loading} from "../elements/Loading";

function OauthLoginPage() {
    const [isLoaded, setIsLoaded] = useState<boolean>(false);
    const [isFetched, setIsFetched] = useState<boolean>(false);
    const location = useLocation();
    const params = location.search;
    const navigate = useNavigate();

    useEffect(() => {
        if (!isLoaded) {
            setIsLoaded(true);
        }
        if (isLoaded && !isFetched) {
            fetchCredentials();
        }
    }, [isLoaded, isFetched]);

    const fetchCredentials = () => {
        getCredentials();
        setIsFetched(true);
    };

    const getCredentials = async () => {
        try {
            const response = await fetch(`/api/login/oauth2/code/${params}`);
            if (response.ok) {
                const data = await response.json();
                const token = data.token;
                const id = data.id;
                localStorage.setItem('userId', id);
                localStorage.setItem('token', token);
                navigate('/home');
            } else {
                console.error('Failed to fetch Google authorization URL');
            }
        } catch (error) {
            console.error('Error fetching Google authorization URL:', error);
        }
    };

    if (!isLoaded) {
        return <Loading />;
    } else {
        return (
            <div className='pageContent'>
                <h1>You Have Successfully Logged In</h1>
            </div>
        );
    }
}

export default OauthLoginPage;

