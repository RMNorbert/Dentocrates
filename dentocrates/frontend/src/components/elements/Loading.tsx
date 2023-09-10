import './Loading.css';
import React from "react";
export const Loading = () => {
    return (
        <>
            <img className={'loadingImg'} src={process.env.PUBLIC_URL + '/dentocrates-website-favicon.png'} alt={'business logo'}/>
            <div className={'loadingText'} >Loading...</div>
        </>
    )
}
