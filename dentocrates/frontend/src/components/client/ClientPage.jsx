import { useEffect, useState } from "react"
export const ClientPage = (clientId) => {
    const [clientData, setClientData] = useState(null);

    const getClientDetails = async() =>{
        const data = await fetch(`/client/${clientId}`);
        setClientData(await data.json());
    }

    useEffect(() => {
    },[clientData]);

    return (
        <div>
            {clientData && clientData.map((client) => {
                <div>
                    <div>Name: {client.firstName} + {client.lastName}</div>
                    <div>Email: {client.email}</div>
                </div>
            })}
        </div>
    )
}
