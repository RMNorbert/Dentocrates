import "./Chat.css"
import React, {useState, useEffect} from 'react';
import { MultiFetch } from "../../utils/fetch/MultiFetch";
function Chat() {
    const iconList = [process.env.PUBLIC_URL + '/help.png',
        process.env.PUBLIC_URL + '/cat.png',
        process.env.PUBLIC_URL + 'cat2.png']
    const indexOfSinglePersonalityChatBotIcon = 0;
    const [isOpen, setIsOpen] = useState(false);
    const [messages, setMessages] = useState([]);
    const [chatIcon, setChatIcon] = useState(iconList[0]);
    const [inputValue, setInputValue] = useState('');
    const [chatWithCat, setChatWithCat] = useState(false);
    const chatUrl = "/chat";
    const { data } = MultiFetch();

    function getRandomInt(max) {
        return Math.floor(Math.random() * max);
    }

    const toggleState = () => {
        setIsOpen((prevState) => !prevState);
        const randomNumber = getRandomInt(3);
        setChatIcon(iconList[randomNumber]);
        setChatWithCat(false);
    };

    const onSendButton = async () => {
        if (inputValue === '') {
            return;
        }
        const msg1 = { name: 'User', message: inputValue };
        setMessages((prevMessages) => [msg1, ...prevMessages]);
        try {
            const requestBody = {message: inputValue, cat: chatWithCat};
            const answer = await data(chatUrl, "POST", requestBody);
            if(answer !== '') {
                const responseMessage = {name: 'AI', message: answer};
                setMessages((prevMessages) => [responseMessage, ...prevMessages]);
            }
            setInputValue('');
        } catch (error) {
            console.error("Error:", error);
        }
    };

    const handleInputChange = (e) => {
        setInputValue(e.target.value);
    };

    const changeChatBotPersonality = () => {
        setChatWithCat(!chatWithCat);
    }

    useEffect(() => {

    }, []);

        return (
            <div className="chat">
            <div className={`${isOpen ? 'chatbox--active' : 'chatbox'}`}>
                <div className="chatbox__support">
                    <div className="chatbox__header">
                        <div className="chatbox__image--header">
                            <img src={chatIcon} className="bot__icon" alt="Chat Support, image created by AI" />
                        </div>
                        <div className="chatbox__content--header">
                            {
                                chatIcon != iconList[indexOfSinglePersonalityChatBotIcon] ?
                                <button className="chatbox__send--footer send__button switch"
                                    onClick={() => changeChatBotPersonality()}>
                                Switch Personality to {chatWithCat? "Normal" : "Cat"}
                                </button> :
                                <></>
                            }
                            <h4 className="chatbox__heading--header">Chat Support</h4>
                            <p className="chatbox__description--header">Hi. My name is DentoBot. How can I help you?</p>
                        </div>
                    </div>
                    <div className="chatbox__messages">
                        {messages.map((message, index) => {
                            return (
                                <div
                                    key={index}
                                    className={`messages__item ${
                                        message.name === 'User'
                                            ? 'messages__item--visitor'
                                            : 'messages__item--operator'
                                    }`}
                                >
                                    {message.message}
                                </div>
                            );
                        })}
                    </div>
                    <div className="chatbox__footer">
                        <input
                            type="text"
                            placeholder="Write a message..."
                            value={inputValue}
                            onChange={handleInputChange}
                        />
                        <button className="chatbox__send--footer send__button" onClick={() => onSendButton()}>
                            Send
                        </button>
                    </div>
                </div>
            </div>
                <div className="chatbox__button">
                    <button className="open__button" onClick={() => toggleState()}>
                        <img
                            className="chatbox__icon"
                            src={process.env.PUBLIC_URL + '/chatbox-icon.png'}
                            alt="chat icon"
                        />
                    </button>
                </div>
            </div>
        );
}

export default Chat;
