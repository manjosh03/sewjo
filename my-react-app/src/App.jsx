import React from 'react'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Fabric from './Fabric.jsx'
import Appbar from './Appbar.jsx'
import FabricList from './FabricList.jsx';


function App() {
  
  return (
    <>
    <Appbar/>
    
    <Router>
            <Routes>
                <Route path="/" element={<Fabric />} />
                <Route path="/fabrics" element={<FabricList />} />
            </Routes>
    </Router>
    </>
  );
};

export default App;

