import React, { useState } from 'react';

import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import Container from '@mui/material/Container';



export default function Fabric() {
    const [name, setName] = useState('')
    const [color, setColor] = useState('')
    const [width, setWidth] = useState()
    const [height, setHeight] = useState()
    const [price, setPrice] = useState()
    const [type, setType] = useState('')
    const handleClick=(e)=> {
        e.preventDefault()
        const fabric={name, color, width, height, price, type}
        console.log(fabric)
    }

  return (
    <React.Fragment>
      <CssBaseline />
      <Container maxWidth="sm">
        
        <h1>Add Fabric</h1>
    <Stack
      component="form"
      sx={{
        width: '25ch',
      }}
      spacing={2}
      noValidate
      autoComplete="off"
    >
      <TextField id="outlined-basic" label="name" variant="outlined" fullWidth 
      value={name}
      onChange={(e)=>setName(e.target.value)}
      />
      <TextField id="outlined-basic" label="color" variant="outlined" fullWidth 
      value={color}
      onChange={(e)=>setColor(e.target.value)}
      />
      <TextField id="outlined-basic" label="width" variant="outlined" fullWidth 
      value={width}
      onChange={(e)=>setWidth(e.target.value)}
      />
      <TextField id="outlined-basic" label="height" variant="outlined" fullWidth 
      value={height}
      onChange={(e)=>setHeight(e.target.value)}
      />
      <TextField id="outlined-basic" label="price" variant="outlined" fullWidth 
      value={price}
      onChange={(e)=>setPrice(e.target.value)}
      />
      <TextField id="outlined-basic" label="type" variant="outlined" fullWidth 
      value={type}
      onChange={(e)=>setType(e.target.value)}
      />
      <Button variant="contained" onClick={handleClick}>Save</Button>
    </Stack>
    
    </Container>
    </React.Fragment>
    
  );
}
