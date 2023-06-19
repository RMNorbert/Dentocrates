import { useEffect, useState } from "react"
import { email } from "../token/TokenDecoder";

export const ClientPage = () => {
    const [clientData, setClientData] = useState(null);

    const getClientDetails = async() =>{
        const data = await fetch(`/client/${email()}`);
        setClientData(await data.json());
    }

    useEffect(() => {
    },[clientData]);

    return (
        <div>
            {clientData && clientData.map((client) => (
                <div>
                    <div>Name: {client.firstName} + {client.lastName}</div>
                    <div>Email: {client.email}</div>
                </div>
            ))}
        </div>
    )
}
