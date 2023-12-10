// AddFoodPage.tsx
import React, {useEffect, useRef, useState} from 'react';
import {
    IonButton,
    IonButtons,
    IonContent,
    IonFab,
    IonFabButton,
    IonHeader,
    IonIcon,
    IonInfiniteScroll,
    IonInfiniteScrollContent,
    IonList,
    IonLoading,
    IonPage,
    IonSearchbar,
    IonTitle,
    IonToolbar,
    SearchbarCustomEvent, ToastOptions,
    useIonModal, useIonToast
} from "@ionic/react";
import CategoryComponent from "../components/CategoryComponent";
import {addOutline, camera, caretBack, fastFoodOutline} from "ionicons/icons";
import axios from "axios";
import {useHistory, useParams} from "react-router";
import {requestPostFoodToMeal} from "../services/actions/diaryDayAction";
import {CategoryProps} from "../types/Category.types";
import {usePhotoGallery} from "../hooks/usePhotoGallery";
import {requestGetMeal} from "../services/actions/mealAction";
import {Food, FoodUpdate, MealDetailsProps} from "../types/MealFood.types";
import "../assets/styles/add-food-page.scss"
import {requestGetAvailableFoods, requestGetFoodsByName, requestPostFood} from "../services/actions/foodAction";
import {useDispatch, useSelector} from "react-redux";
import {loadingReduce, removeSegmentationResult, RootState, setSegmentationResult} from "../store";
import MealItemComponent from "../components/MealItemComponent";
import {calculateCaloriesForQuantity} from "../services/utils";
import {OverlayEventDetail} from "@ionic/core/components";
import AddFoodModal from "../components/AddFoodModal";
import {baseURL} from "../services/actions";
import {errorOptions} from "../services/toastOptions";

export interface RouteParams {
    diaryDay: string,
    mealId: string,
    foodId: string
}


const ListingFoodPage: React.FC = () => {
    const dispatch = useDispatch();
    const loginState = useSelector((state: RootState) => state.login);
    const isLoading = useSelector((state: RootState) => state.loading).isLoading
    const segmentedImage = useSelector((state: RootState) => state.diaryDay.segmentationResult)
    const categoryResult = useSelector((state: RootState) => state.diaryDay.categoryResults)
    const token = loginState.token;
    const history = useHistory();
    const {diaryDay, mealId} = useParams<RouteParams>()
    const [mealDetails, setMealDetails] = useState<MealDetailsProps>();
    const {photoBase64, takePhoto} = usePhotoGallery();
    const [page, setPage] = useState<number>(0);
    const [availableFoods, setAvailableFoods] = useState<Food[]>([]);
    const [allPagesFetched, setAllPagesFetched] = useState<boolean>(false);
    const [searchFoodName, setSearchFoodName] = useState<string>('');
    const refToTop = useRef<HTMLHeadingElement>(null);
    const [ionToast] = useIonToast();
    const presentIonToast = (options: ToastOptions) => {
        ionToast(options);
    };
    const handleAddFoodToMeal = async (diaryDayDate: string, mealId: string, foodId: number, quantityId: number, token: string) => {
        return await requestPostFoodToMeal(diaryDayDate, mealId, foodId, quantityId, token);
    }
    const handleGetMeal = async (mealId: string) => {
        return await requestGetMeal(mealId, token);
    }

    function fetchAvailableFoods() {
        requestGetAvailableFoods(page, token)
            .then(response => {
                const newAvailableFoods = response.data;
                setAvailableFoods((prevState) => [...prevState, ...newAvailableFoods])
                setPage((prevPage) => prevPage + 1)
                if (newAvailableFoods.length < 20) {
                    setAllPagesFetched(true);
                }
            })
            .catch(err => presentIonToast(errorOptions))
    }

    function fetchFoodsByName(searchFoodName: string) {
        requestGetFoodsByName(searchFoodName, token)
            .then(response => setAvailableFoods(response?.data))
            .catch(err => presentIonToast(errorOptions))
    }

    useEffect(() => {
        fetchAvailableFoods();
    }, [])

    async function loadMore(event: CustomEvent<void>) {
        setTimeout(() => {
            fetchAvailableFoods();
            (event.target as HTMLIonInfiniteScrollElement).complete();
        }, 1000);
    }


    useEffect(() => {
        handleGetMeal(mealId).then((response) => {
            setMealDetails(response.data);
        }).catch(err => presentIonToast(errorOptions))
    }, [mealId])

    const handleSearch = (event: SearchbarCustomEvent) => {
        let inputFoodName = event.target.value;
        setSearchFoodName(inputFoodName ? inputFoodName : '');
        if (!!inputFoodName) {
            setAvailableFoods([])
            setPage(0);
            setAllPagesFetched(true);
            fetchFoodsByName(inputFoodName);
        } else {
            setAvailableFoods([])
            setPage(0);
            setAllPagesFetched(false);
            fetchAvailableFoods();
        }
    };
    const [present, dismiss] = useIonModal(AddFoodModal, {
        onDismiss: (data: FoodUpdate, role: string) => dismiss(data, role),
        foodName: searchFoodName
    });

    function openModal() {
        present({
            onWillDismiss: (ev: CustomEvent<OverlayEventDetail>) => {
                if (ev.detail.role === 'confirm') {
                    requestPostFood(searchFoodName, ev.detail.data.proteinPerCent,
                        ev.detail.data.carbohydratePerCent, ev.detail.data.lipidPerCent,
                        token).then((response) => {
                            setAvailableFoods([...availableFoods, response.data]);
                        }
                    ).catch(err => {
                        presentIonToast(errorOptions)
                    });

                }
            },
        });
    }

    function uploadImageByUser(imageUploadedByUser: Blob) {
        const formData = new FormData();
        formData.append('image', imageUploadedByUser);
        let sendImage = axios.post(baseURL + '/image', Object.fromEntries(formData), {
            headers: {
                'Content-type': 'multipart/form-data'
            }
        });
        dispatch(loadingReduce({isLoading: true}))
        sendImage.then(r => {
            const categoryList: CategoryProps[] = r.data.category;
            const base64ImageString = r.data.overlay;
            handleSetSegmentationResult(base64ImageString, categoryList);

        }).catch(err => presentIonToast(errorOptions)).finally(() => dispatch(loadingReduce(({isLoading: false}))))
    }

    const quantity = 100.0

    const handleRemoveSegmentationResult = () => {
        dispatch(removeSegmentationResult())
    }

    const handleSetSegmentationResult = (segmentationResult: string, categoryResult: CategoryProps[]) => {
        dispatch(setSegmentationResult({segmentationResult: segmentationResult, categoryResults: categoryResult}))
    }
    return (
        <IonPage>
            <IonHeader>
                <IonToolbar>
                    <IonButtons slot="start">
                        <IonButton onClick={() => {
                            handleRemoveSegmentationResult();
                            history.goBack();
                        }}>
                            <IonIcon icon={caretBack}></IonIcon>
                        </IonButton>
                    </IonButtons>
                    <IonTitle>{mealDetails?.name}</IonTitle>
                </IonToolbar>
            </IonHeader>
            <IonLoading isOpen={isLoading} message="Loading..." spinner="circles"/>
            <IonContent id="content" className="add-meal-page ion-padding" fullscreen>
                <h1 ref={refToTop}></h1> {/* add a ref to the h1 element */}
                {
                    photoBase64 ? (
                        <img src={`data:image/jpeg;base64,${photoBase64}`} width={"256px"} height={"256px"}
                             alt="Your food"/>
                    ) : (
                        <></>
                    )}
                <div>
                    {segmentedImage ? (
                        <div className="food-selection-section">
                            <img src={`data:image/png;base64,${segmentedImage}`} alt="Your meal segmented"/>
                            <IonList>
                                {
                                    categoryResult && categoryResult.length > 0 ? (
                                            <div>
                                                <p>Is this what you are eating?</p>
                                                {
                                                    categoryResult.map(categoryProps =>
                                                        <CategoryComponent key={categoryProps.categoryId}
                                                                           categoryId={categoryProps.categoryId}
                                                                           categoryColor={categoryProps.categoryColor}
                                                                           mealId={parseInt(mealId, 10)}
                                                                           onAddFoodToMealClick={(foodId, quantity) => handleAddFoodToMeal(diaryDay, mealId, foodId, quantity, token)}
                                                        />)
                                                }
                                            </div>)
                                        : <div>Sorry! Didn't find any food :(</div>
                                }
                            </IonList>
                        </div>
                    ) : (
                        <></>
                    )}
                </div>
                <div className="food-selection-section">
                    <IonSearchbar placeholder="Search for a food"
                                  value={searchFoodName}
                                  show-clear-button="focus"
                                  onIonChange={event => handleSearch(event)}/>
                    <IonList>
                        {
                            availableFoods.length > 0 ?
                                availableFoods.map(food =>
                                    <MealItemComponent key={food.id} id={food.id} handleAction={(foodId) => {
                                        history.push(`/add-food/${diaryDay}/${mealId}/${foodId}`)
                                    }}
                                                       name={food.name}
                                                       quantity={quantity}
                                                       calories={calculateCaloriesForQuantity(food.protein, food.carbohydrate, food.lipid, quantity)}/>
                                ) :
                                <div style={{
                                    display: "flex",
                                    justifyContent: "center",
                                    flexDirection: "column",
                                    textAlign: "center"
                                }}>
                                    <p>No results.</p>
                                    <p>Help us to improve.</p>
                                    <IonButton style={{width: "100%", height: "50px"}} onClick={openModal}>
                                        <IonIcon icon={addOutline}></IonIcon>
                                        Add your food
                                    </IonButton>
                                </div>
                        }
                    </IonList>
                    <div style={{
                        position: "fixed",
                        bottom: "20px",
                        right: "20px",
                    }}>
                        <IonFab slot="bottom" vertical="bottom" horizontal="end">
                            <IonFabButton onClick={() => {
                                takePhoto().then((response) => {
                                    uploadImageByUser(convertBase64ToBlob(response.base64String!));
                                    if (refToTop.current) {
                                        setTimeout(() => {
                                            if (refToTop.current)
                                                refToTop.current.scrollIntoView({behavior: 'smooth'})
                                        }, 100)
                                    }
                                })
                            }}>
                                <IonIcon icon={camera}/>
                            </IonFabButton>
                        </IonFab>
                    </div>
                    <IonInfiniteScroll disabled={allPagesFetched}
                                       onIonInfinite={(event: CustomEvent<void>) => loadMore(event)}>
                        <IonInfiniteScrollContent loadingSpinner={null}
                                                  loadingText="Loading more foods...">
                            <div>
                                <IonIcon icon={fastFoodOutline} size="large" className="my-rotate"/>
                            </div>
                        </IonInfiniteScrollContent>
                    </IonInfiniteScroll>
                </div>
            </IonContent>
        </IonPage>
    );

};

/**
 * Convert BASE64 to BLOB
 * @param base64Image Pass Base64 image data to convert into the BLOB
 */
function convertBase64ToBlob(base64Image: string) {
    const decodedData = atob(base64Image);
    const uInt8Array = new Uint8Array(decodedData.length);
    for (let i = 0; i < decodedData.length; ++i) {
        uInt8Array[i] = decodedData.charCodeAt(i);
    }
    return new Blob([uInt8Array], {type: "image/jpeg"});
}

export default ListingFoodPage;