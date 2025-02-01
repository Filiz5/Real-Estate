"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import "bootstrap-icons/font/bootstrap-icons.css";
import styles from "@/styles/components/dashboards/my-adverts/my-adverts.module.scss";
import 'bootstrap/dist/css/bootstrap.min.css';
import { Button } from 'react-bootstrap';
import Image from "next/image";
import { Paginations } from "@/components/common/Paginations";
import { IoIosSearch } from 'react-icons/io';
import { swAlert, swConfirm } from "@/helpers/swal";
import { deleteAdvertAuthUser } from "@/services/advert-service";

const MyAdverts = ({data}) => {
  const {  number, size, totalPages } = data.object;
  const [adverts, setAdverts] = useState([]);
  const [search, setSearch] = useState('');
  const [currentUrl, setCurrentUrl] = useState('');
  const router = useRouter();

  useEffect(() => {
    const url = new URL(window.location.href);
    setCurrentUrl(url.pathname);
    if(search){
      url.searchParams.set('q', search);
    }else{
      url.searchParams.delete('q');
    }
    setAdverts(data.object.content);
    router.push(url.toString());

  }, [data, search, router]);

  const handleEditClick = (id) => {
    if (id) {
      router.push(`/dashboard/my-adverts/edit/${id}`);
    }
  };

  const handleDeleteClick = async (id) => {
    try {
        const answer = await swConfirm('Are you sure you want to delete this advert?');
        if (!answer.isConfirmed) return;

        const response = await deleteAdvertAuthUser(id);
        if(response.success){
          swAlert(response.message, 'success');
          router.refresh();
        }else{
          swAlert(response.message, 'error');
        }
      
    } catch (error) {
      swAlert(error.message, 'error');
    }
  }

  return (
    <main className={styles.container}>
      <div className={styles.search_container}>
        <input
          type="text"
          placeholder="Search"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <IoIosSearch size={40} color="7286D3" />
      </div>
      <div className={styles.table_container}>
        {/* Table Header */}
        <div className={styles.table_header}>
          <div className={styles.table_cell}>
            <strong>#</strong>
          </div>
          <div className={styles.table_cell}>
            <strong>Property</strong>
          </div>
          <div className={styles.table_cell}>
            <strong>Publish Date</strong>
          </div>
          <div className={styles.table_cell}>
            <strong>Status</strong>
          </div>
          <div className={styles.table_cell}>
            <strong>Views/Likes/Tours</strong>
          </div>
          <div className={styles.table_cell}>
            <strong>Actions</strong>
          </div>
        </div>

        {/* Table Rows */}
        {adverts.map((advert, index) => (
          <div key={index} className={styles.table_row}>
            <div className={styles.table_cell}>{index + 1}</div>

            <div className={styles.table_cell}>
              <div className={styles.advert_image_container}>
                <div >
                  <Image
                    src={advert.image.data} // Add MIME type dynamically
                    alt={advert.image.name}
                    className={styles.advert_image}
                    width="150"
                    height="150"
                    style={{ objectFit: 'cover', border: '1px solid #ddd' }}
                  />
                </div>
                <div className={styles.advert_details}>
                  <div className={styles.advert_title}>{advert.title}</div>
                  <div className={styles.advert_adress}>
                    {`${advert.country.name}, ${advert.city.name}` ||
                      'Address not available'}
                  </div>
                  <div className={styles.advert_price}>${advert.price}</div>
                </div>
              </div>
            </div>

            <div className={styles.table_cell}>
              {advert.createdAt
                ? advert.createdAt.split('T')[0]
                : 'Not Available'}
            </div>

            <div className={styles.table_cell}>
              <span className={styles.status}>{advert.advertStatus}</span>
            </div>

            <div className={styles.table_cell}>
              <div className={styles.icon_container}>
                <span className="views">
                  <i className="bi bi-eye" style={{ color: 'blue' }}></i>{' '}
                  {advert.viewCount }
                </span>
                <span className={styles.likes}>
                  <i className="bi bi-heart" style={{ color: 'red' }}></i>{' '}
                  {advert.favoriteCount }
                </span>
                <span className="tours">
                  <i className="bi bi-geo-alt" style={{ color: 'green' }}></i>{' '}
                  {advert.tourRequestCount }
                </span>
              </div>
            </div>

            <div className={styles.button_cell}>
              <Button
                className="edit-btn"
                onClick={() => handleEditClick(advert.id)}
              >
                <i className="bi bi-pencil"></i> {/* Edit Icon */}
              </Button>
              <Button
                className={styles.delete_btn}
                onClick={() => handleDeleteClick(advert.id)}
              >
                <i className="bi bi-trash"></i> {/* Delete Icon */}
              </Button>
            </div>
          </div>
        ))}
      </div>
      <Paginations
        baseUrl={currentUrl}
        currentPage={number + 1}
        size={size}
        totalPages={totalPages}
      />
    </main>
  );
};


export default MyAdverts;
