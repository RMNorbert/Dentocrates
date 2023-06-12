import { useEffect, useState } from "react"
export const DentistPage = (dentistId) => {
    const [dentistData, setDentistData] = useState(null);

    const getDentistDetails = async() =>{
        const data = await fetch(`/pass/active/${dentistId}`);
        setDentistData(await data.json());
    }

    useEffect(() => {

    },[]);

    return (
        <div>
            {dentistData && dentistData.map((dentist) => {
                <div>
                    <h1>Dentist:</h1>
                    <div>{dentist.firstName} + {dentist.lastName}</div>
                </div>
            })}
        </div>
    )
}
