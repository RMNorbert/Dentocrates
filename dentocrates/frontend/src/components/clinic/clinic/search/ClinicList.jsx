import { useNavigate } from "react-router-dom";
export const ClinicList = ({ clinicDatas }) => {
    const navigate = useNavigate();
    return (
        <div>
            {clinicDatas &&
                clinicDatas.map((clinic) => {
                    const clinicUrl = `/clinic/${clinic.id}`
                    return (
                        <div
                            key={clinic.name}
                            className="listBox"
                            onClick={() => navigate(clinicUrl)}
                        >
                            <div>
                            <h1 className="listMargin">{clinic.name}</h1>
                            </div>
                            <h2 className="listDetail1 listMargin">{clinic.city} {clinic.street} str.</h2>
                            <h3 className="listDetail2 listMargin">{clinic.clinicType.replaceAll("_"," ")}</h3>
                            <h3 className="listDetail3 listMargin">Open: {clinic.openingHours}</h3>
                        </div>
                    );
                })}
        </div>
    );
};