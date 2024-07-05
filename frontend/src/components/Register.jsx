import  { useState } from 'react';
import axios from 'axios';

function Register() {
  const [username, setUserName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
 
  async function save(e) {
    e.preventDefault();
    try {
        const user = {
            username: username,
            email: email,
            password: password
        };

        const res = await fetch("http://localhost:8080/api/v1/user/save", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(user),
        });

        if (!res.ok) {
            // Handle the case where the request was not successful
            const errorData = await res.json();
            alert('Error: ${errorData.message}');
            return;
        }

        const data = await res.json();
        console.log("User saved successfully:", data);
    } catch (error) {
        alert('Network Error: ${error.message}');
    }
}
  

  return (
    <div>
      <div className ="container mt-4">
        <div className="card">
          <h1>USER REGISTERATION</h1>

      <form>
          <div className="form-group">
            <label>Name</label>
            <input type="text" className="form-control" id="username" placeholder="Enter Name"
            value={username}
            onChange={(event)=>{
              setUserName(event.target.value);
            }}
            />

          </div>

           <div className="form-group">
            <label>email</label>
            <input type="email" className="form-control" id="email" placeholder="Enter Email"

            value={email}
            onChange={(event)=> {
              setEmail(event.target.value);
            }}
            />
            </div> 

            <div className="form-group">
                <label>password</label>
                <input type="password" className="form-control" id="password" placeholder="Enter Password"

                value={password}
                onChange={(event)=>{
                  setPassword(event.target.value);
                }}
                />
            </div>

            <button type = "submit" className = "btn btn-primary mt-4" onClick={save}>Sign Up</button>

      </form>

        </div>
      </div>
    </div>
  );
}

export default Register;
