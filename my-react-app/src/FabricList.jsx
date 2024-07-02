import React, { useEffect, useState } from 'react';
import { Container, Typography, Grid, Card, CardContent, CardActions, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';
import { useNavigate } from 'react-router-dom';

export default function FabricsList() {
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

    return (
        <Container maxWidth="md" style={{ marginTop: '64px'}}>
            <Typography variant="h4" gutterBottom style={{ marginBottom: '16px' }}>
                All Fabrics
            </Typography>
            <Grid container spacing={3}>
                {fabrics.map((fabric, index) => (
                    <Grid item xs={12} sm={6} md={4} key={index}>
                        <Card>
                            {/* Replace with image upload/display components */}
                            <CardContent>
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
                            <CardActions>
                                {/* action button */}
                                <Button size="small">Edit</Button>
                            </CardActions>
                        </Card>
                    </Grid>
                ))}
            </Grid>
            <Button 
                variant="outlined" 
                onClick={handleGoBack} 
                style={{
                    position: 'fixed',
                    bottom: '20px',
                    left: '20px',
                    zIndex: 1,
                }}
            >
                Back to Add Fabric
            </Button>
        </Container>
    );
}
