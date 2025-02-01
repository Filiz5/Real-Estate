import Image from "next/image";
import React, { useState, useMemo } from "react";
import { Container, Pagination, Table, Button } from "react-bootstrap";
import { FaEdit, FaTrash } from "react-icons/fa";
import Swal from "sweetalert2";
import { deleteAdvert } from "@/actions/admin-user";
import { useRouter } from 'next/navigation';

const AdvertList = ({ userAdverts }) => {
  const router = useRouter();
  const [page, setPage] = useState(1);
  const [pageSize] = useState(5);
  const [adverts, setAdverts] = useState(userAdverts);

  const paginatedAdverts = useMemo(() => {
    return adverts.slice((page - 1) * pageSize, page * pageSize);
  }, [adverts, page, pageSize]);

  const totalPages = Math.ceil(adverts.length / pageSize);

  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  const handleDelete = async (id) => {
    Swal.fire({
      title: "Are you sure?",
      text: "This action cannot be undone!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Yes, delete it!",
      cancelButtonText: "Cancel",
      reverseButtons: true,
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          await deleteAdvert(id);
          setAdverts((prevAdverts) => prevAdverts.filter((advert) => advert.id !== id));
          Swal.fire("Deleted!", "The advert has been deleted.", "success");
        } catch (error) {
          console.error("Error deleting advert:", error);
          Swal.fire("Error!", "Failed to delete the advert. Please try again.", "error");
        }
      }
    });
  };

  const handleEdit = (id) => {
    router.push(`/admin-dashboard/admin-adverts/edit/${id}`);
  };

  return (
    <Container fluid="true">
      <Table striped responsive>
        <thead>
          <tr>
            <th>Property</th>
            <th>Date Published</th>
            <th>Status</th>
            <th>View/Like/Tour</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {paginatedAdverts.map((advert) => (
            <tr key={advert.id}>
              <td>
                <div className="d-flex align-items-center">
                  <Image
                    src={advert.images[0]?.data || "/default-image.jpg"}
                    alt={advert.title}
                    className="advert-image"
                    width={500}
                    height={800}
                  />
                  <div>
                    <p>{advert.title || "No Title"}</p>
                    <p>{advert.location || "Unknown Location"}</p>
                    <p>${advert.price || "Unknown Price"}</p>
                  </div>
                </div>
              </td>
              <td>{new Date(advert.createdAt).toLocaleDateString()}</td>
              <td>
                <span className="badge bg-primary">{advert.advertStatus}</span>
              </td>
              <td>
                <div className="d-flex flex-column align-items-center">
    <div>
      👁️ {advert.viewCount}
    </div>
    <div>
      ❤️ {advert.favorites.length || 0}
    </div>
    <div>
      📍 {advert.tourRequests.length || 0}
    </div>
                  </div>
              </td>

              <td>
                <Button
                  className="btn btn-link action-button"
                  onClick={() => handleDelete(advert.id)}
                >
                  <FaTrash />
                </Button>
                <Button className="btn btn-link action-button gap-2"
                onClick={() => handleEdit(advert.id)}
                  >
                  <FaEdit />
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      <div className="mt-4 d-flex justify-content-end">
        <Pagination className="custom-pagination">
          <Pagination.First
            onClick={() => handlePageChange(1)}
            disabled={page === 1}
          />
          <Pagination.Prev
            onClick={() => handlePageChange(page - 1)}
            disabled={page === 1}
          />
          <Pagination.Item>
            {page} / {totalPages}
          </Pagination.Item>
          <Pagination.Next
            onClick={() => handlePageChange(page + 1)}
            disabled={page === totalPages}
          />
          <Pagination.Last
            onClick={() => handlePageChange(totalPages)}
            disabled={page === totalPages}
          />
        </Pagination>
      </div>
    </Container>
  );
};

export default AdvertList;
