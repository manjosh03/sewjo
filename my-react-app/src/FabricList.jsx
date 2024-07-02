import React, { useEffect, useState } from 'react';
import { Container, Typography, Grid, Card, CardContent, CardActions, Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';

export default function FabricList() {
    const [fabrics, setFabrics] = useState([]);
    const [sortBy, setSortBy] = useState('name'); // Default sorting by name
    const [sortOrder, setSortOrder] = useState('asc'); // Default ascending order
    const [filterByType, setFilterByType] = useState(''); // Default no filter by type
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
            const updatedFabrics = fabrics.filter(fabric => fabric.id !== id);
            setFabrics(updatedFabrics);
        })
        .catch(error => console.error('Error deleting fabric:', error));
    };

    const handleSortChange = (event) => {
        const { value } = event.target;
        setSortBy(value);
        // Call sorting function
        sortFabrics(value, sortOrder);
    };

    const handleOrderChange = (event) => {
        const { value } = event.target;
        setSortOrder(value);
        // Call sorting function
        sortFabrics(sortBy, value);
    };

    const sortFabrics = (sortBy, order) => {
        let sortedFabrics = [...fabrics];
        sortedFabrics.sort((a, b) => {
            if (order === 'asc') {
                return a[sortBy] > b[sortBy] ? 1 : -1;
            } else {
                return a[sortBy] < b[sortBy] ? 1 : -1;
            }
        });
        setFabrics(sortedFabrics);
    };

    const handleTypeFilter = (event) => {
        const { value } = event.target;
        setFilterByType(value);
    };

    const filteredFabrics = fabrics.filter(fabric => {
        if (filterByType === '') {
            return true; // Show all fabrics if no type filter is selected
        } else {
            return fabric.type.toLowerCase() === filterByType.toLowerCase();
        }
    });

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
            <div style={{ marginBottom: '20px' }}>
                <label>Sort by:</label>
                <select value={sortBy} onChange={handleSortChange}>
                    <option value="name">Name</option>
                    <option value="color">Color</option>
                    <option value="width">Width</option>
                    <option value="height">Height</option>
                    <option value="price">Price</option>
                    <option value="type">Type</option>
                </select>
                <select value={sortOrder} onChange={handleOrderChange}>
                    <option value="asc">Ascending</option>
                    <option value="desc">Descending</option>
                </select>
            </div>
            <div style={{ marginBottom: '20px' }}>
                <label>Filter by Type:</label>
                <select value={filterByType} onChange={handleTypeFilter}>
                    <option value="">All Types</option>
                    <option value="cotton">Cotton</option>
                    <option value="silk">Silk</option>
                    <option value="polyester">Polyester</option>
                    <option value="wool">Wool</option>
                    {/* Add other fabric types as options */}
                </select>
            </div>
            <Grid container spacing={3}>
                {filteredFabrics.map((fabric, index) => (
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
