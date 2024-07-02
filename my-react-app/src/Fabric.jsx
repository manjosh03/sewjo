import React, { useState } from 'react';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import Container from '@mui/material/Container';
import Box from '@mui/material/Box';

export default function Fabric() {
    const [name, setName] = useState('');
    const [color, setColor] = useState('');
    const [width, setWidth] = useState('');
    const [height, setHeight] = useState('');
    const [price, setPrice] = useState('');
    const [type, setType] = useState('');
    
    const handleClick = (e) => {
        e.preventDefault();
        const fabric = { name, color, width, height, price, type };
        console.log(fabric);
    };

    return (
        <React.Fragment>
            <CssBaseline />
            <Container maxWidth="sm">
                <Box
                    display="flex"
                    justifyContent="center"
                    alignItems="center"
                    minHeight="100vh"
                >
                    <Stack
                        component="form"
                        sx={{
                            width: '100%',
                            maxWidth: '400px', // Adjust the width as needed
                            padding: '20px',
                            boxShadow: 3,
                            borderRadius: 2,
                            backgroundColor: 'white',
                        }}
                        spacing={2}
                        noValidate
                        autoComplete="off"
                    >
                        <h1>Add Fabric</h1>
                        <TextField
                            id="outlined-basic"
                            label="Name"
                            variant="outlined"
                            fullWidth
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                        />
                        <TextField
                            id="outlined-basic"
                            label="Color"
                            variant="outlined"
                            fullWidth
                            value={color}
                            onChange={(e) => setColor(e.target.value)}
                        />
                        <TextField
                            id="outlined-basic"
                            label="Width"
                            variant="outlined"
                            fullWidth
                            value={width}
                            onChange={(e) => setWidth(e.target.value)}
                        />
                        <TextField
                            id="outlined-basic"
                            label="Height"
                            variant="outlined"
                            fullWidth
                            value={height}
                            onChange={(e) => setHeight(e.target.value)}
                        />
                        <TextField
                            id="outlined-basic"
                            label="Price"
                            variant="outlined"
                            fullWidth
                            value={price}
                            onChange={(e) => setPrice(e.target.value)}
                        />
                        <TextField
                            id="outlined-basic"
                            label="Type"
                            variant="outlined"
                            fullWidth
                            value={type}
                            onChange={(e) => setType(e.target.value)}
                        />
                        <Button variant="contained" onClick={handleClick}>
                            Save
                        </Button>
                    </Stack>
                </Box>
            </Container>
        </React.Fragment>
    );
}
