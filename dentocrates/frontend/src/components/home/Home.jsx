import Chat from "../chat/Chat";

function Home() {
    const description = "Dentocrates is an innovative online platform designed to revolutionize the way patients find and schedule dental appointments.\n" +
        "Our user-friendly interface and advanced features are aimed at providing a seamless experience for both patients and dental practitioners.\n" +
        "At Dentocrates, we understand the challenges patients face in finding suitable dental care. Our platform acts as a centralized\n" +
        "appointment system for individual clinics, making it easier than ever to locate available appointments. With Dentocrates, users gain access to a comprehensive network of dental professionals and clinics, ensuring they can find the care they need, even if their preferred dentist is unavailable."
    return (
        <div className='home'>
            <div className='homeContent'>
            <h1>Welcome to Dentocrates</h1>
            <h2>An online platform for dental appointments</h2>
                <h3 className="homeDescription">
                    {description}
                </h3>
            </div>
            <img
                className="home-logo"
                src={process.env.PUBLIC_URL + '/dentocrates-light-logo.png'}
                width={"280px"}
                alt="logo"
            />
            <footer className="footer">
            <Chat/>
            </footer>
        </div>
    )
}

export default Home;
