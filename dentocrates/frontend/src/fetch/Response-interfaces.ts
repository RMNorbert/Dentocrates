interface LocationDTO {
    zipCode: number;
    city: string;
}
interface AppointmentDTO {
    id: number;
    clinicId: number;
    customerId: number;
    reservation: string;
    appeared: boolean;
}

interface ClinicResponseDTO {
    id: number;
    name: string;
    clinicType: string;
    website: string;
    contactNumber: string;
    zipCode: number;
    city: string;
    street: string;
    openingHours: string;
    dentistId: number;
}
interface Leave {
    id: number;
    clinicId: number;
    startOfTheLeave: string;
    endOfTheLeave: string;
}

interface CustomerAppointmentResponseDTO {
    id: number;
    firstName: string;
    lastName: string;
}
interface CustomerResponseDTO {
    id: number;
    email: string;
    firstName: string;
    lastName: string;
    role: string;
}

interface DentistResponseDTO {
    id: number;
    email: string;
    firstName: string;
    lastName: string;
    role: string;
    operatingLicenceNo: string;
}
interface ClinicData {
    name: string;
    clinicType: string;
    contactNumber: string;
    website: string;
    zipCode: number;
    city: string;
    street: string;
    openingHours: string;
    dentistId: number | null;
}
