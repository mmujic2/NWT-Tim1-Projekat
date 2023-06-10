/*<div className="form-group mb-3">
                    <label className="text-white">Select File</label>
                    <input type="file" className="form-control" name="image" onChange={onFileChange} />
                </div>
                
                <div className="d-grid">
                   <button type="submit" className="btn btn-outline-primary" onClick={()=>{}}>Store</button>
                </div>
    <img src={image}/>*/


// Ja sam dirao, imas problem sa tim?
    const [image,setImage] = useState("")
    const onFileChange = (e)=> {
        let files = e.target.files;

        let fileReader = new FileReader();

        fileReader.readAsDataURL(files[0]);
 

        fileReader.onload = (event) => {

            setImage(event.target.result)

            console.log(event.target.result)
            
        }
    }
