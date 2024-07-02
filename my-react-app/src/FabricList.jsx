import React, { useEffect, useState } from 'react';
import { Container, Typography, Grid, Card, CardContent, CardActions, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';
import { useNavigate } from 'react-router-dom';

export default function FabricList() {
    const [fabrics, setFabrics] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        fetch('http://localhost:8080/fabric/view')
        .then(res => res.json())
        .then((result) => {
            setFabrics(result);
        });
    }, []);

    const handleGoBack = () => {
        navigate('/');
    };

    const handleDelete = (id) => {
        fetch(`http://localhost:8080/fabric/delete/${id}`, {
            method: 'DELETE',
        })
        .then(() => {
            // Update fabrics state after deletion
            //const updatedFabrics = fabrics.filter(fabric => fabric.id !== id);
            //setFabrics(updatedFabrics);
        })
        .catch(error => console.error('Error deleting fabric:', error));
    };

    return (
        <Container maxWidth="md" style={{ marginTop: '40px', position: 'relative' }}>
            <Button 
                variant="outlined" 
                onClick={handleGoBack} 
                style={{
                    position: 'absolute',
                    right: '20px',
                    zIndex: 1,
                }}
            >
                Back to Add Fabric
            </Button>
            <Typography variant="h4" gutterBottom style={{ marginBottom: '40px' }}>
                All Fabrics
            </Typography>
            <Grid container spacing={3}>
                {fabrics.map((fabric, index) => (
                    <Grid item xs={12} sm={6} md={4} key={index}>
                        <Card>
                            {/* Replace with image upload/display components */}
                            <img src="http://via.placeholder.com/150" alt="fabric pic" style={{ display: 'block', margin: 'auto', marginTop: '30px'  }} />
                            <CardContent style={{ textAlign: 'center' }}>
                                <Typography variant="h5" component="div">
                                    {fabric.name}
                                </Typography>
                                <Typography variant="body2" color="text.secondary">
                                    Color: {fabric.color}<br />
                                    Width: {fabric.width}<br />
                                    Height: {fabric.height}<br />
                                    Price: {fabric.price}<br />
                                    Type: {fabric.type}
                                </Typography>
                            </CardContent>
                            <CardActions style={{ justifyContent: 'space-between' }}>
                                <Button size="small">Edit</Button>
                                <Button size="small" onClick={() => handleDelete(fabric.id)}>Delete</Button>
                            </CardActions>
                        </Card>
                    </Grid>
                ))}
            </Grid>
            
        </Container>
    );
}
