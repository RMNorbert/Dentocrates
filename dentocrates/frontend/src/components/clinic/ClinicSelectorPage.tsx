import "./ClinicSelectorPage.css";
import React, { useEffect, useState } from "react"
import { ClinicList } from "./ClinicList"
import { Loading } from "../elements/Loading";
import { MultiFetch } from "../../fetch/MultiFetch";
export const ClinicSelectorPage: React.FC = () => {
    const [isDataLoaded, setIsDataLoaded] = useState<boolean>(false);
    const [clinicData, setClinicData] = useState<ClinicResponseDTO[]>([]);
    const [filteredData, setFilteredData] = useState<ClinicResponseDTO[]>([]);

    const getClinicData = async () => {
        const clinicDataUrl = `/clinic/all`;
        const response = await MultiFetch<ClinicResponseDTO[]>(clinicDataUrl);
        setClinicData(response);
        setFilteredData(response);
        setIsDataLoaded(true);
    };

    const search = (event: React.ChangeEvent<HTMLInputElement>) => {
        const searchText = event.target.value;
        const filteredClinics = clinicData.filter((clinic) =>
            clinic.name.toLowerCase().includes(searchText.toLowerCase())
        );
        setFilteredData(filteredClinics);
    };

    useEffect(() => {
        if (!isDataLoaded) {
            getClinicData();
        }
    }, [isDataLoaded]);

    if (isDataLoaded) {
        return (
            <div className="selector">
                <div>
                    <input
                        className="searchBar"
                        type="text"
                        placeholder="Search for clinics"
                        onChange={(event) => search(event)}
                    />
                </div>
                <div className="list">
                    {filteredData && filteredData.length > 0 ? (
                        <ClinicList clinicDatas={filteredData} />
                    ) : (
                        <p>No results found.</p>
                    )}
                </div>
            </div>
        );
    } else {
        return <Loading />;
    }
};

