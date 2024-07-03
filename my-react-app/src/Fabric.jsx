import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import Container from '@mui/material/Container';
import Box from '@mui/material/Box';
import InputAdornment from '@mui/material/InputAdornment';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';

export default function Fabric() {
    const [name, setName] = useState('');
    const [color, setColor] = useState('');
    const [width, setWidth] = useState('');
    const [height, setHeight] = useState('');
    const [price, setPrice] = useState('');
    const [type, setType] = useState('');
    const navigate = useNavigate();

    // Define fabric types
    const fabricTypes = ['Cotton', 'Silk', 'Polyester', 'Wool']; // Add more as needed

    const handleClick = (e) => {
        e.preventDefault();
        const fabric = { name, color, width, height, price, type };
        
        fetch('http://localhost:8080/fabric/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(fabric)
        })
        .then(response => response.json())
        .then(data => console.log('Success:', data))
        .catch((error) => console.error('Error:', error));
    };

    const handleViewFabrics = () => {
        navigate('/fabrics');
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
                    flexDirection="column"
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
                            type="number"
                            variant="outlined"
                            fullWidth
                            value={width}
                            onChange={(e) => setWidth(e.target.value)}
                        />
                        <TextField
                            id="outlined-basic"
                            label="Height"
                            type="number"
                            variant="outlined"
                            fullWidth
                            value={height}
                            onChange={(e) => setHeight(e.target.value)}
                        />
                        <TextField
                            id="outlined-basic"
                            label="Price"
                            type="number"
                            variant="outlined"
                            defaultValue="$"
                            fullWidth
                            value={price}
                            onChange={(e) => setPrice(e.target.value)}
                            InputProps={{
                                startAdornment: (
                                    <InputAdornment position="start">$</InputAdornment>
                                ),
                            }}
                        />
                        <FormControl fullWidth variant="outlined">
                            <InputLabel id="fabric-type-label">Type</InputLabel>
                            <Select
                                labelId="fabric-type-label"
                                id="fabric-type"
                                value={type}
                                onChange={(e) => setType(e.target.value)}
                                label="Type"
                            >
                                {fabricTypes.map((type, index) => (
                                    <MenuItem key={index} value={type}>{type}</MenuItem>
                                ))}
                            </Select>
                        </FormControl>
                        <Button variant="contained" onClick={handleClick}>
                            Save
                        </Button>
                        <Button variant="contained" onClick={handleViewFabrics} style={{ marginTop: '10px' }}>
                            View All Fabrics
                        </Button>
                    </Stack>
                </Box>
            </Container>
        </React.Fragment>
    );
}
