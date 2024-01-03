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
                            className="listBox shadowBorder"
                            onClick={() => navigate(clinicUrl)}
                        >
                            <div>
                            <h1 className="listMargin shadowLightBorder">{clinic.name}</h1>
                            </div>
                            <h2 className="listDetail1 listMargin shadowLightBorder">{clinic.city} {clinic.street} str.</h2>
                            <h3 className="listDetail2 listMargin shadowLightBorder">{clinic.clinicType.replaceAll("_"," ")}</h3>
                            <h3 className="listDetail3 listMargin shadowLightBorder">Open: {clinic.openingHours}</h3>
                        </div>
                    );
                })}
        </div>
    );
};
