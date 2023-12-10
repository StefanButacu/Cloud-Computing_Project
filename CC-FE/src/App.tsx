import {Redirect, Route} from 'react-router-dom';

import {IonApp, setupIonicReact} from '@ionic/react';
import {IonReactRouter} from '@ionic/react-router';

/* Core CSS required for Ionic components to work properly */
import '@ionic/react/css/core.css';

/* Basic CSS for apps built with Ionic */
import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';

/* Optional CSS utils that can be commented out */
import '@ionic/react/css/padding.css';
import '@ionic/react/css/float-elements.css';
import '@ionic/react/css/text-alignment.css';
import '@ionic/react/css/text-transformation.css';
import '@ionic/react/css/flex-utils.css';
import '@ionic/react/css/display.css';

/* Theme variables */
import './theme/variables.css';
import ListingFoodPage from "./pages/ListingFoodPage";
import LoginPage from "./pages/LoginPage";

import AddFoodPage from "./pages/AddFoodPage";
import EditFoodPage from "./pages/EditFoodPage";
import React from "react";
import {RegisterPage} from "./pages/RegisterPage";
import {useSelector} from "react-redux";
import {RootState} from "./store";
import MainTabs from "./pages/MainTabs";

setupIonicReact();

const App: React.FC = () => {

    const {isAuthenticated} = useSelector((state: RootState) => state.login)
    return (
        <IonApp>
            <IonReactRouter>
                <Route exact path="/register" component={RegisterPage}/>
                <Route exact path="/login" component={LoginPage}/>
                <Route path="/" render={() => {
                    return isAuthenticated ? <MainTabs/> : <Redirect to={"/login"}/>;
                }}/>
                <Route
                    exact
                    path="/add-food/:diaryDay/:mealId/:foodId"
                    render={(props) => {
                        return isAuthenticated ? <AddFoodPage/> : <Redirect to={"/login"}/>;
                    }}
                />


                <Route
                    exact
                    path="/edit-food/:diaryDay/:mealId/:foodId"
                    render={(props) => {
                        return isAuthenticated ? <EditFoodPage/> : <Redirect to={"/login"}/>;
                    }}
                />

                <Route
                    exact
                    path="/add-food/:diaryDay/:mealId"
                    render={(props) => {
                        return isAuthenticated ? <ListingFoodPage/> : <Redirect to={"/login"}/>;
                    }}
                />
            </IonReactRouter>
        </IonApp>)
};

export default App;
