import React, {useEffect, useState} from "react"
import {Loading} from "../elements/Loading";
import {MultiFetch} from "../../fetch/MultiFetch";
import {useNavigate} from "react-router-dom";

export const DentistPage = () => {
    const { data } = MultiFetch();
    const navigate = useNavigate();
    const [isDataLoaded, setIsDataLoaded] = useState<boolean>(false);
    const [dentistData, setDentistData] = useState<DentistResponseDTO[]>([]);
    const [filteredData, setFilteredData] = useState<DentistResponseDTO[]>([]);
    const [clinicData, setClinicData] = useState<ClinicResponseDTO[]>([]);
    const getDentistDetails = async() =>{
        await getClinicData();
        const responseData = await data(`/dentist/all`);
        setDentistData(responseData);
        setFilteredData(responseData);
        setIsDataLoaded(true);
    }
    const getClinicData = async () => {
        const responseData = await data(`/clinic/all`);
        setClinicData(responseData);
    }

    const filterClinics = (currentName: string, currentId: number) => {
        return clinicData
            .filter((clinic) => clinic.dentistId === currentId)
            .map((relatedClinic) => (
                <div key={relatedClinic.street} onClick={() => navigate(`/clinic/${relatedClinic.id}`)}>
                    <h1 className="listName listMargin">{currentName}</h1>
                    <div className="listDetail1 listMargin">
                        {relatedClinic.clinicType.replaceAll("_"," ")}
                    </div>
                    <div className="listDetail2 listMargin">
                        Location: {relatedClinic.city} {relatedClinic.street}
                    </div>
                </div>
            ));
    };

    const search = (event: React.ChangeEvent<HTMLInputElement>) => {
        const searchText = event.target.value;
        const filteredDentists = dentistData.filter((dentist) =>
            dentist.lastName.toLowerCase().includes(searchText.toLowerCase())
        );
        setFilteredData(filteredDentists);
    };

    useEffect(() => {
        if (!isDataLoaded) {
            getDentistDetails();
        }
    }, [isDataLoaded]);

    if (isDataLoaded) {
        return (
            <div className="selector">
                <div>
                    <input
                        className="searchBar"
                        type="text"
                        placeholder="Search for dentist"
                        onChange={(event) => search(event)}
                    />
                </div>
                <div className="list">
                    {filteredData.length > 0 ? (
                        filteredData.map((dentist) => {
                            const name = `Dentist: Dr.${dentist.firstName} ${dentist.lastName}`;
                            return (
                                <div  key={dentist.id} className="listBox">
                                    {filterClinics(name, dentist.id)}
                                </div>
                            );
                        })
                    ) : (
                        <p>No results found.</p>
                    )}
                </div>
            </div>
        );
    } else {
        return <Loading />;
    }
}
