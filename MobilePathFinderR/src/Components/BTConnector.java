
package Components;

import java.util.Vector;

import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

public class BTConnector implements DiscoveryListener {

  public static final int BLUETOOTH_TIMEOUT = 30000;

  public Vector btDevicesFound;
  public Vector btServicesFound;

  private boolean isBTSearchComplete;

  static BTConnector btManager = null;

  static BTConnector instance() {
    if (btManager == null) {
      btManager = new BTConnector();
    }

    return btManager;
  }

  /** Creates a new instance of BTConnector */
  private BTConnector() {
    btDevicesFound = new Vector();
    btServicesFound = new Vector();
  }

  public static UUID[] getRFCOMM_UUID() {
    UUID[] uuidSet;
    UUID RFCOMM_UUID = new UUID(0x1101); // RFCOMM service
    uuidSet = new UUID[1];
    uuidSet[0] = RFCOMM_UUID;

    return uuidSet;
  }

  /**
   * Finds bluetooth devices
   * 
   * @param aServices,
   *          an array with the service UUID identifiers you want to search
   * @returns the number of devices found
   */
  public int find(UUID[] aServices) {
    findDevices();
    findServices(aServices);
    return btDevicesFound.size();
  }

  public int findDevices() {
    try {
      // cleans previous elements
      btDevicesFound.removeAllElements();
      isBTSearchComplete = false;
      LocalDevice local = LocalDevice.getLocalDevice();
/**/      local.setDiscoverable(DiscoveryAgent.GIAC);
      
      DiscoveryAgent discoveryAgent = local.getDiscoveryAgent();
      // discover new devices
      discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);
      while ((!isBTSearchComplete)) {
        // waits for a fixed time, to avoid long search
        synchronized (this) {
          this.wait(BTConnector.BLUETOOTH_TIMEOUT);
        }
        // check if search is completed
        if (!isBTSearchComplete) {
          // search no yet completed so let's cancel it
          discoveryAgent.cancelInquiry(this);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return btDevicesFound.size();
  }

  public void deviceDiscovered(RemoteDevice remoteDevice,
      DeviceClass deviceClass) {
    btDevicesFound.addElement(remoteDevice);
  }

  public void inquiryCompleted(int param) {
    isBTSearchComplete = true;
    // notifies and wake main thread that device search is completed
    synchronized (this) {
      this.notify();
    }
  }

  public void findServices(UUID[] aServices) {
    // cleans previous elements
    btServicesFound.removeAllElements();
    try {
      LocalDevice local = LocalDevice.getLocalDevice();
      DiscoveryAgent discoveryAgent = local.getDiscoveryAgent();
      // discover services
      if (btDevicesFound.size() > 0) {
        for (int i = 0; i < btDevicesFound.size(); i++) {
          isBTSearchComplete = false;
          // adds a null element in case we don't found service
          btServicesFound.addElement(null);
          int transID = discoveryAgent.searchServices(null, aServices,
              (RemoteDevice) (btDevicesFound.elementAt(i)), this);
          // wait for service discovery ends
          synchronized (this) {
            this.wait(BTConnector.BLUETOOTH_TIMEOUT);
          }
          if (!isBTSearchComplete) {
            discoveryAgent.cancelServiceSearch(transID);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  Vector getservices(){
  
  return btServicesFound;
  }
  
  
  
  
  public void servicesDiscovered(int param, ServiceRecord[] serviceRecord) {
    int index = btServicesFound.size() - 1;
    for (int i = 0; i < serviceRecord.length; i++) {
      btServicesFound.setElementAt(serviceRecord[i], index);
    }
  }

  public void serviceSearchCompleted(int transID, int respCode) {
    isBTSearchComplete = true;
    // notifies and wake mains thread that service search is completed
    synchronized (this) {
      this.notify();
    }
  }

  /**
   * Get a human readable name of the BT device.
   * 
   * @param deviceID
   * @return the friendly name for the BT device
   */
  public String getDeviceName(int deviceID) {
    try {
      return ((RemoteDevice) btDevicesFound.elementAt(deviceID))
          .getFriendlyName(false);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "Error";
  }

  /**
   * Gets the URL address of the the service you want to connect
   * 
   * @param deviceID
   * @return the Url address for the device and service found
   */
  public String getServiceURL(int deviceID) {
    try {
      return ((ServiceRecord) btServicesFound.elementAt(deviceID))
          .getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "Error";
  }

}
