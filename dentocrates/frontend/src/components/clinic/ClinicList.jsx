import { useNavigate } from "react-router-dom";
export const ClinicList = (clinicData) => {
    const navigate = useNavigate();
    return (
        <div>asdasd
            {clinicData && clinicData.map((clinic) => {
                <div
                    key={clinic.name}
                    onClick={() => navigate(`/clinic/${clinic.id}`)}
                >
                    <h1>{clinic.name}</h1>
                    <h2>Location : {clinic.city} {clinic.street}</h2>
                    <h3>{clinic.clinicType}</h3>
                    <h3>Open: {clinic.openingHours}</h3>
                </div>
            })}
        </div>
    )
}
