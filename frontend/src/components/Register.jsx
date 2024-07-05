import  { useState } from 'react';
import axios from 'axios';

function Register() {
  const [username, setUserName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
 
async function save(event){
  event.preventDefault();
 // try {
    await axios.post("http://192.168.56.1:8080/api/v1/user/save" ,( {
      username : username,
      email : email,
      password : password,
    })).then((response) => console.log(response));
    alert("User Registeration Success!");
//  } catch (err) {
 //   alert(err);
//  }
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
