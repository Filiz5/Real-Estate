export function convertBase64ToImages(data) {
  if (data) {
    return `data:image/jpeg;base64,${data}`;
  }
  return null;
}
