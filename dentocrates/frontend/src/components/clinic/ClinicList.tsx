import { useNavigate } from "react-router-dom";
import React from "react";
interface ClinicListProps {
    clinicDatas: ClinicResponseDTO[];
}
export const ClinicList: React.FC<ClinicListProps> = ({ clinicDatas }) => {
    const navigate = useNavigate();

    return (
        <div>
            {clinicDatas &&
                clinicDatas.map((clinic) => {
                    return (
                        <div
                            key={clinic.name}
                            className="listBox"
                            onClick={() => navigate(`/clinic/${clinic.id}`)}
                        >
                            <div>
                                <h1 className="listMargin">{clinic.name}</h1>
                            </div>
                            <h2 className="listDetail1 listMargin">{clinic.city} {clinic.street} str.</h2>
                            <h3 className="listDetail2 listMargin">{clinic.clinicType.replaceAll("_", " ")}</h3>
                            <h3 className="listDetail3 listMargin">Open: {clinic.openingHours}</h3>
                        </div>
                    );
                })}
        </div>
    );
};
