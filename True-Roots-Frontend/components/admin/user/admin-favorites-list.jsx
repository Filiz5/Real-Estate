import React, { useState, useMemo } from "react";
import { Container, Pagination, Button } from "react-bootstrap";
import { FaTrash } from "react-icons/fa";
import { deleteFavorite } from "@/actions/admin-user";
import Swal from "sweetalert2";
import Image from "next/image";

const FavoritesList = ({ userFavorites }) => {
  const [favorites, setFavorites] = useState(userFavorites);
  const [page, setPage] = useState(1);
  const [pageSize] = useState(5);

  const paginatedFavorites = useMemo(() => {
    return favorites.slice((page - 1) * pageSize, page * pageSize);
  }, [favorites, page, pageSize]);

  const totalPages = Math.ceil(favorites.length / pageSize);

  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  const handleDeleteFavorite = async (id) => {
    const result = await Swal.fire({
      title: "Are you sure?",
      text: "Do you really want to delete this favorite?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Yes, delete it!",
      cancelButtonText: "No, keep it",
    });

    if (result.isConfirmed) {
      try {
        await deleteFavorite(id);
        setFavorites((prevFavorites) => prevFavorites.filter(fav => fav.id !== id));
        Swal.fire("Deleted!", "Your favorite has been deleted.", "success");
      } catch (error) {
        console.error('Error deleting favorite:', error);
        Swal.fire("Error!", "An error occurred while deleting the favorite.", "error");
      }
    }
  };

  if (!favorites || favorites.length === 0) {
    return <p>No favorites available.</p>;
  }

  return (
    <Container fluid="true" className="mt-5">
      <table className="table table-striped">
        <thead className="thead-light">
          <tr>
            <th>Property</th>
            <th>Category</th>
            <th>Date Added</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {paginatedFavorites.map((favorite) => (
            <tr key={favorite.id}>
              <td>
                <div className="d-flex align-items-center">
                  <Image
                    src={favorite.advert.images[0]?.data || "/default-image.jpg"}
                    alt={favorite.title}
                    className="advert-image"
                    width={500} 
                    height={300}
                  />
                  <div className="ms-3">
                    <p>{favorite.advert.title}</p>
                    <p>{favorite.advert.description}</p>
                    <p>{favorite.advert.location}</p>
                    <p>${favorite.advert.price}</p>
                  </div>
                </div>
              </td>
              <td>{favorite.advert.category?.title || "N/A"}</td>
              <td>{new Date(favorite.advert.createdAt).toLocaleDateString()}</td>
              <td>
                <Button
                  variant="link"
                  onClick={() => handleDeleteFavorite(favorite.id)}
                  className="btn btn-link action-button"
                >
                  <FaTrash />
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="d-flex justify-content-end mt-4">
        <Pagination className="custom-pagination">
          <Pagination.First onClick={() => handlePageChange(1)} disabled={page === 1} />
          <Pagination.Prev onClick={() => handlePageChange(page - 1)} disabled={page === 1} />
          <Pagination.Item>{page} / {totalPages}</Pagination.Item>
          <Pagination.Next onClick={() => handlePageChange(page + 1)} disabled={page === totalPages} />
          <Pagination.Last onClick={() => handlePageChange(totalPages)} disabled={page === totalPages} />
        </Pagination>
      </div>
    </Container>
  );
};

export default FavoritesList;