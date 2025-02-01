"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import "bootstrap-icons/font/bootstrap-icons.css";
import styles from "@/styles/components/dashboards/my-favorites/my-favorites.module.scss";
import 'bootstrap/dist/css/bootstrap.min.css';
import { Button } from 'react-bootstrap';
import Image from "next/image";
import { IoIosSearch } from 'react-icons/io';
import { toggleFavorite } from "@/services/favorite-service";
import Swal from 'sweetalert2';
import { Paginations } from "@/components/common/Paginations";

const MyFavorites = ({ data }) => {
  const { number, size, totalPages} = data.object;
  const [adverts, setAdverts] = useState([]);
  const [search, setSearch] = useState('');
  const [currentUrl, setCurrentUrl] = useState('');
  const router = useRouter();
  const [filteredAdverts, setFilteredAdverts] = useState([]);

  useEffect(() => {
    if (typeof window !== 'undefined') {
      try {
        const url = new URL(window.location.href);
        setCurrentUrl(url.pathname);
        if (search) {
          url.searchParams.set('q', search);
        } else {
          url.searchParams.delete('q');
        }

        if (data && Array.isArray(data.object.content)) {
          const favoritesData = data.object.content.map(item => {
            if (item.advert) {
              return {
                id: item.advert.id,
                title: item.advert.title,
                desc: item.advert.desc,
                price: item.advert.price,
                location: item.advert.location,
                images: item.advert.images,
                country: item.advert.country,
                city: item.advert.city,
                advertStatus: item.advert.advertStatus,
                createdAt: item.advert.createdAt,
                views: item.advert.viewCount,
                likes: item.advert.favorites.length,
                tours: item.advert.tourRequests.length,
                category: item.advert.category,
                type: item.advert.advertType,
                slug: item.advert.slug,
              };
            } else {
              console.error("Advert is undefined for item:", item);
              return null;
            }
          }).filter(Boolean);

          setAdverts(favoritesData);
          setFilteredAdverts(favoritesData);
          router.push(url.toString());
        } else {
          console.error("Data object is not an array or is undefined:", data);
          setAdverts([]);
          setFilteredAdverts([]);
        }
        router.push(url.toString());
      } catch (error) {
        console.error("Error processing favorites data:", error);
        setAdverts([]);
        setFilteredAdverts([]);
      }
    }
  }, [data, search, router]);

  const handleFavoriteClick = async (id, isFavorited) => {
    if (id) {
      try {
        await toggleFavorite(id);

        const message = isFavorited 
          ? "Removed from favorites!" 
          : "Added to favorites!";

        Swal.fire({
          icon: 'success',
          title: message,
          showConfirmButton: false,
          timer: 1500
        });

        setTimeout(() => {
          window.location.reload();
        }, 1250);
      } catch (error) {
        console.error("Error toggling favorite:", error);
      }
    }
  };

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  return (
    <main className={styles.container}>
      <div className={styles.search_container}>
        <input
          type="text"
          placeholder="Search"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <IoIosSearch size={40} color="#7286D3" />
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
            <strong>Category</strong>
          </div>
          <div className={styles.table_cell}>
            <strong>Type</strong>
          </div>
          <div className={styles.table_cell}>
            <strong>Views/Likes/Tours</strong>
          </div>
          <div className={styles.table_cell}>
            <strong>Actions</strong>
          </div>
        </div>

        {/* Table Rows */}
          {filteredAdverts.length > 0 ? (
            filteredAdverts.map((advert, index) => (
              <div key={advert.id} className={styles.table_row} onClick={() => router.push(`/properties/${advert.slug}`)} style={{ cursor: 'pointer' }}>
                <div className={styles.table_cell}>{index + 1}</div>

                <div className={styles.table_cell}>
                  <div className={styles.advert_image_container}>
                    {advert.images && advert.images.length > 0 ? (
                      advert.images.map((image, i) =>
                        image.featured ? (
                          <div key={i}>
                            <Image
                              src={
                                image.featured === true
                                  ? image.data
                                  : '/placeholder-image.png'
                              }
                              alt={image.name}
                              className={styles.advert_image}
                              width={150}
                              height={150}
                              style={{ objectFit: 'cover', border: '1px solid #ddd' }}
                            />
                          </div>
                        ) : null
                      )
                    ) : (
                      <div>No images available</div>
                    )}
                    <div className={styles.advert_details}>
                      <div className={styles.advert_title}>
                        {advert.title}
                      </div>
                      <div className={styles.advert_address}>
                        {advert.country && advert.city
                          ? `${advert.country.name}, ${advert.city.name}`
                          : 'Address not available'}
                      </div>
                      <div className={styles.advert_price}>${advert.price}</div>
                    </div>
                  </div>
                </div>

                <div className={styles.table_cell}>
                  <span className={styles.status}>{advert.category.title}</span>
                </div>

                <div className={styles.table_cell}>
                  <span className={styles.status}>{advert.type.title}</span>
                </div>

                <div className={styles.table_cell}>
                  <div className={styles.icon_container}>
                    <span className="views">
                      <i className="bi bi-eye" style={{ color: 'blue' }}></i>{' '}
                      {advert.views || 0}
                    </span>
                    <span className={styles.likes}>
                      <i className="bi bi-heart" style={{ color: 'red' }}></i>{' '}
                      {advert.likes || 0}
                    </span>
                    <span className="tours">
                      <i className="bi bi-geo-alt" style={{ color: 'green' }}></i>{' '}
                      {advert.tours || 0}
                    </span>
                  </div>
                </div>

                <div className={styles.table_cell}>
                  <Button
                    className="favorite-btn"
                    onClick={(e) => { e.stopPropagation(); handleFavoriteClick(advert.id, advert.likes > 0); }}
                    style={{ backgroundColor: 'red', borderColor: 'red' }}
                  >
                    <i className={`bi bi-heart${advert.likes > 0 ? '' : '-outline'}`}></i>
                  </Button>
                </div>
              </div>
            ))
          ) : (
            <div className={styles.no_adverts}>No favorites found.</div>
          )}
      </div>
       {
        totalPages.length >=1 &&
        <Paginations
        baseUrl={currentUrl}
        currentPage={number +1}
        size={size}
        totalPages={totalPages}
      /> 
       }   
      
    </main>
  );
};

export default MyFavorites;