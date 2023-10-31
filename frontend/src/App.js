import './App.css';
import React, { useEffect, useState } from 'react';
import { ForceGraph2D } from 'react-force-graph';
import { addPersons, addGroups, fetchGroupsAndPersons, addPersonToGroup } from './dataService';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';
import DataGrid, { Column } from 'devextreme-react/data-grid';
import 'devextreme/dist/css/dx.common.css';
import 'devextreme/dist/css/dx.light.css';

function App() {
  const [data, setData] = useState({ nodes: [], links: [] });
  const [dataTrigger, setDataTrigger] = useState(0);
  const [groupsWithPersonCount, setGroupsWithPersonCount] = useState();

  useEffect(() => {
    fetchGroupsAndPersons()
      .then(axios.spread((groupsResponse, personsResponse) => {
        const groupsData = groupsResponse.data;
        const personsData = personsResponse.data;

        setGroupsWithPersonCount(groupsData.map(group => ({
          id: group.id,
          name: group.name,
          personCount: group.persons ? group.persons.length : 0
        }))
        )

        const findPersonById = id => personsData.find(person => person.id === id) || { id };

        const groupNodes = groupsData.map(group => ({
          id: `group-${group.id}`,
          name: `${group.id} ${group.name}`,
          color: '#FF0000'  // Red for groups
        }));

        const personNodes = personsData.map(personOrId => {
          const person = typeof personOrId === 'object' ? personOrId : findPersonById(personOrId);
          return {
            id: `person-${person.id}`,
            name: `${person.id} ${person.name}`,
            color: '#00FF00'  // Green for persons
          };
        });

        const groupLinks = groupsData.flatMap(group =>
          (group.persons || []).map(personOrId => {
            const person = typeof personOrId === 'object' ? personOrId : findPersonById(personOrId);
            return {
              source: `group-${group.id}`,
              target: `person-${person.id}`
            };
          })
        );

        setData({
          nodes: [...groupNodes, ...personNodes],
          links: groupLinks
        });
      }))
      .catch(error => {
        console.error("Error fetching data:", error);
      });
  }, [dataTrigger]);

  const LoadGroups = () => {
    addGroups()
      .then(() => {
        setDataTrigger(dataTrigger + 1);
      })
      .catch(error => {
        console.error("Error adding group:", error);
      });
  }

  const LoadPersons = () => {
    addPersons()
      .then(() => {
        setDataTrigger(dataTrigger + 1);
      })
      .catch(error => {
        console.error("Error adding person:", error);
      });
  }

  const LoadPersonToGroup = () => {
    addPersonToGroup()
      .then(() => {
        setDataTrigger(dataTrigger + 1);
      })
      .catch(error => {
        console.error("Error adding person to group:", error);
      });
  }

  return (
    <>
      <div className="page-margin">
        <div className="d-flex flex-column align-items-start mb-2">

          <div className="d-flex mb-2">
            <input type="text" id="add-group" placeholder="type new group" className="form-control short-input mr-2" />
            <button onClick={LoadGroups} className="btn btn-primary">add group</button>
          </div>

          <div className="d-flex mb-2">
            <input type="text" id="add-person" placeholder="type new person" className="form-control short-input mr-2" />
            <button onClick={LoadPersons} className="btn btn-primary">add person</button>
          </div>

          <div className="d-flex align-items-center">
            <input type="number" id="personId" placeholder="type person id" className="form-control short-input mr-2" />
            <input type="number" id="groupId" placeholder="type group id" className="form-control short-input mr-2" />
            <button onClick={LoadPersonToGroup} className="btn btn-success">add person to group</button>
          </div>

        </div>


        <div style={{ border: '2px solid black', width: '804px', height: '550px' }}>
          <ForceGraph2D
            width={800}
            height={550}
            graphData={data}
            nodeAutoColorBy="color"
            nodeLabel="name"
            linkDirectionalArrowLength={3.5}
            linkDirectionalArrowRelPos={1}
          />
        </div>
      </div>

      <DataGrid
        dataSource={groupsWithPersonCount}
        showBorders={true}
      >
        <Column dataField="id" width={50} />
        <Column dataField="name" />
        <Column dataField="personCount" caption="Number of Persons" />
      </DataGrid>
    </>

  );
}

export default App;
